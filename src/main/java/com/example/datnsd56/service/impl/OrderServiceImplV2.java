package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.repository.*;
import com.example.datnsd56.security.UserInfoUserDetails;
import com.example.datnsd56.service.AccountService;
import com.example.datnsd56.service.CartService;
import com.example.datnsd56.service.OrderSeriveV2;
import com.example.datnsd56.service.VoucherUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImplV2 implements OrderSeriveV2 {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private UserInfoUserDetails userInfoUserDetails;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private VoucherSeviceImpl voucherService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private VoucherUsageRepository voucherUsageRepository;
    @Autowired
    private TransactionsServiceIpml transactionsService;
    @Autowired
    private VoucherUsageService voucherUsageService;
    @Autowired
    private  VoucherUsageHistoryRepository voucherUsageHistoryRepository;



    @Transactional
    @Override
    public void applyVoucherWithoutSaving(Orders order, String voucherCode, String selectedVoucherCode) {
        if (voucherCode != null && !voucherCode.isEmpty()) {
            Optional<Voucher> voucherOptional = voucherService.findByCode(voucherCode);
            if (voucherOptional.isPresent()) {
                Voucher voucher = voucherOptional.get();
                boolean canUseVoucher = canUseVoucher(order.getAccountId(), voucher);

                if (canUseVoucher) {
                    BigDecimal discountValue = calculateDiscountValue(voucher, order.getTotal());
                    BigDecimal discountedTotal = order.getTotal().subtract(discountValue);
                    order.setTotal(discountedTotal);

                    order.setVoucher(voucher);
                }
            }
        } else if (selectedVoucherCode != null && !selectedVoucherCode.isEmpty()) {
            Optional<Voucher> selectedVoucherOptional = voucherService.findByCode(selectedVoucherCode);
            if (selectedVoucherOptional.isPresent()) {
                Voucher selectedVoucher = selectedVoucherOptional.get();
                boolean canUseVouchers = canUseVoucher(order.getAccountId(), selectedVoucher);

                if (canUseVouchers) {
                    BigDecimal discountValue = calculateDiscountValue(selectedVoucher, order.getTotal());
                    BigDecimal discountedTotal = order.getTotal().subtract(discountValue);
                    order.setTotal(discountedTotal);

                    order.setVoucher(selectedVoucher);
                }
            }
        }
    }

    @Override
    public void cancelVoucher(Cart cart, String voucherCode) {
        if (cart != null && voucherCode != null && !voucherCode.isEmpty()) {
            List<Orders> orders = cart.getAccountId().getOrders(); // Lấy đơn hàng từ giỏ hàng

            if (orders != null && !orders.isEmpty()) {
                Optional<Voucher> voucher = voucherService.findByCode(voucherCode); // Thay thế bằng phương thức tương ứng

                if (voucher.isPresent() && voucher.get().getCode().equals(voucherCode)) {
                    // Sử dụng danh sách để lưu trữ các phần tử cần loại bỏ
                    List<Orders> ordersToRemove = new ArrayList<>();

                    for (Orders order : orders) {
                        if (order.getVoucher() != null && order.getVoucher().equals(voucher.get())) {
                            order.setVoucher(null);
                            order.setTotal(order.getTotalWithoutDiscount()); // Cập nhật lại tổng tiền
                            // Lưu đơn hàng sau khi hủy áp dụng voucher
                            ordersRepository.save(order);
                            ordersToRemove.add(order);
                        }
                    }

                    // Loại bỏ các phần tử khỏi danh sách sau khi vòng lặp kết thúc
                    orders.removeAll(ordersToRemove);
                }
            }
        }
    }



    @Override
    public BigDecimal calculateTotalWithVoucher(Cart cart, String selectedVoucherCode, String username) {
        // Lấy thông tin giỏ hàng
        BigDecimal total = cart.getTotalPrice();

        // Kiểm tra xem voucher có tồn tại và có hợp lệ hay không
        Optional<Voucher> voucher = voucherService.findByCode(selectedVoucherCode);

        if (voucher.isPresent() && voucher.get().isActive() && voucher.get().getExpiryDateTime() != null &&
            LocalDateTime.now().isBefore(voucher.get().getExpiryDateTime())) {

            // Kiểm tra xem voucher đã được sử dụng bởi tài khoản nào đó hay chưa
            boolean isVoucherUsed = voucherUsageService.isVoucherUsed(username, selectedVoucherCode);

            if (!isVoucherUsed) {
                // Áp dụng giảm giá của voucher vào tổng tiền
                BigDecimal discountAmount = calculateDiscountValue(voucher.get(), total);
                total = total.subtract(discountAmount);
            }
        }

        return total;
    }


    @Override
    public List<VoucherUsage> findByIsVisibleTrue() {
        return voucherUsageRepository.findByIsVisibleTrue();
    }




    @Transactional
    @Override
    public Orders placeOrder(Cart cart, String address, String voucherCode, String selectedVoucherCode) {
        Orders order = createOrder(cart, address);
        if (order == null) {
            return null;
        }

        // Xử lý chi tiết đơn hàng và giảm số lượng sản phẩm
        processOrderDetails(cart, order);

        // Áp dụng voucher nếu có
        applyVoucher(order, voucherCode, selectedVoucherCode);

        // Lưu đơn hàng vào cơ sở dữ liệu
        try {
            order = ordersRepository.save(order);

            // Lưu lịch sử sử dụng voucher và đánh dấu khi đặt hàng
//            saveVoucherUsageHistoryOnOrder(order, order.getVoucher());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return order;
    }
    private void saveVoucherUsageHistoryOnOrder(Orders order, Voucher voucher) {
        VoucherUsageHistory voucherUsageHistory = new VoucherUsageHistory();
        voucherUsageHistory.setAccount(order.getAccountId());
        voucherUsageHistory.setVoucher(voucher);
        voucherUsageHistory.setUsedDate(LocalDateTime.now());
        voucherUsageHistoryRepository.save(voucherUsageHistory);
    }
    // ...

    @Transactional
    @Override
    public void applyVoucher(Orders order, String voucherCode, String selectedVoucherCode) {
        if ((voucherCode != null && !voucherCode.isEmpty()) && (selectedVoucherCode != null && !selectedVoucherCode.isEmpty())) {
            // Xử lý trường hợp cả hai điều kiện đều đúng
            // Ứng với tài khoản, bạn cần xác định thứ tự ưu tiên hoặc xử lý sao nếu cả hai voucher đều có thể áp dụng.
        } else if (voucherCode != null && !voucherCode.isEmpty()) {
            Optional<Voucher> voucherOptional = voucherService.findByCode(voucherCode);
            if (voucherOptional.isPresent()) {
                Voucher voucher = voucherOptional.get();
                boolean canUseVoucher = canUseVoucher(order.getAccountId(), voucher);

                if (canUseVoucher) {
                    // Áp dụng voucher từ mã
                    applyVoucherByCode(order, voucher);
                }
            }
        } else if (selectedVoucherCode != null && !selectedVoucherCode.isEmpty()) {
            Optional<Voucher> selectedVoucherOptional = voucherService.findByCode(selectedVoucherCode);
            if (selectedVoucherOptional.isPresent()) {
                Voucher selectedVoucher = selectedVoucherOptional.get();
                boolean canUseVouchers = canUseVoucher(order.getAccountId(), selectedVoucher);

                if (canUseVouchers) {
                    // Áp dụng voucher từ danh sách
                    applyVoucherFromList(order, selectedVoucher);
                }
            }
        }
    }

    // ...

    private void applyVoucherByCode(Orders order, Voucher voucher) {
        BigDecimal discountValue = calculateDiscountValue(voucher, order.getTotal());
        BigDecimal discountedTotal = order.getTotal().subtract(discountValue);
        order.setTotal(discountedTotal);

        // Đánh dấu voucher là đã sử dụng
        markVoucherAsUsed(order.getAccountId(), voucher);
        saveVoucherUsageHistorys(order.getAccountId(), voucher);
        order.setVoucher(voucher);
    }

    private void applyVoucherFromList(Orders order, Voucher voucher) {
        BigDecimal discountValue = calculateDiscountValue(voucher, order.getTotal());
        BigDecimal discountedTotal = order.getTotal().subtract(discountValue);
        order.setTotal(discountedTotal);

        // Đánh dấu voucher là đã sử dụng
        markVoucherAsUsed(order.getAccountId(), voucher);
        saveVoucherUsageHistorys(order.getAccountId(), voucher);
        order.setVoucher(voucher);
    }

    // ...

    private void markVoucherAsUsed(Account account, Voucher voucher) {
        // Đánh dấu voucher là đã sử dụng trong bảng VoucherUsage
        List<VoucherUsage> voucherUsages = voucherUsageRepository.findByAccountAndVoucher(account, voucher);
        for (VoucherUsage voucherUsage : voucherUsages) {
            if (!voucherUsage.getIsUsed()) {
                voucherUsage.setIsUsed(true);
                voucherUsage.setIsVisible(false);
                voucherUsageRepository.save(voucherUsage);


                return;  // Nếu tìm thấy và đánh dấu, thoát khỏi hàm để tránh đánh dấu nhiều lần.
            }
        }
    }


    private void saveVoucherUsageHistorys(Account account, Voucher voucher) {
        // Lưu lịch sử sử dụng voucher vào bảng VoucherUsageHistory
        VoucherUsageHistory voucherUsageHistory = new VoucherUsageHistory();
        voucherUsageHistory.setAccount(account);
        voucherUsageHistory.setVoucher(voucher);
        voucherUsageHistory.setUsedDate(LocalDateTime.now());
        voucherUsageHistoryRepository.save(voucherUsageHistory);
    }

    // ...




    private boolean canUseVoucher(Account account, Voucher voucher) {
        // Kiểm tra xem voucher có được sử dụng bởi tài khoản hay không
        List<VoucherUsage> voucherUsages = voucherUsageRepository.findByAccountAndVoucher(account, voucher);
        for (VoucherUsage usage : voucherUsages) {
            if (!usage.getIsUsed() && !isVoucherExpired(voucher)) {
                return true; // Tài khoản có thể sử dụng voucher này
            }
        }
        return false; // Tài khoản không thể sử dụng voucher này
    }
//    private boolean canUseVouchers(Account account, String selectedVoucherCode ) {
//        // Kiểm tra xem voucher có được sử dụng bởi tài khoản hay không
//        List<VoucherUsage> voucherUsages = voucherUsageRepository.findByAccountAndVouchers(account, selectedVoucherCode);
//        for (VoucherUsage usage : voucherUsages) {
//            if (!usage.getIsUsed() ) {
//                return true; // Tài khoản có thể sử dụng voucher này
//            }
//        }
//        return false; // Tài khoản không thể sử dụng voucher này
//    }
    private boolean isVoucherExpired(Voucher voucher) {
        // Kiểm tra xem voucher có hết hạn hay không
        LocalDateTime currentDateTime = LocalDateTime.now();
        return voucher.getExpiryDateTime() != null && currentDateTime.isAfter(voucher.getExpiryDateTime());
    }
//    private boolean isVoucherExpireds(String selectedVoucherCode) {
//        // Kiểm tra xem voucher có hết hạn hay không
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        return voucher.getExpiryDateTime() != null && currentDateTime.isAfter(selectedVoucherCode.getExpiryDateTime());
//    }

        private boolean canUseVouchers(Account account, Voucher voucher) {
            // Kiểm tra xem voucher có được sử dụng bởi tài khoản hay không
            Set<VoucherUsage> voucherUsages = voucher.getVoucherUsages();

            for (VoucherUsage usage : voucherUsages) {
                if (usage.getAccount().equals(account) && !usage.getIsUsed()) {
                    return true; // Nếu tìm thấy voucher chưa sử dụng của tài khoản
                }
            }

            return false; // Tài khoản không thể sử dụng voucher này
        }









    @Transactional
@Override
 public    Orders createOrder(Cart cart, String address) {
        // Tạo mới đối tượng Orders và thiết lập thông tin cần thiết
        Orders bill = new Orders();
        bill.setAddress(address);
        bill.setPhone(cart.getAccountId().getPhone());
        bill.setEmail(cart.getAccountId().getEmail());
        bill.setFullname(cart.getAccountId().getName());
        bill.setShippingFee(BigDecimal.ZERO);

        bill.setTotal(cart.getTotalPrice().setScale(2, RoundingMode.HALF_UP));

        bill.setOrderStatus(10);
        bill.setCreateDate(LocalDate.now());
        bill.setUpdateDate(LocalDate.now());
        bill.setAccountId(cart.getAccountId());

        // Lưu đơn hàng vào cơ sở dữ liệu
        try {
            return ordersRepository.save(bill);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    @Override

    public void processOrderDetails(Cart cart, Orders order) {
        // Xử lý chi tiết đơn hàng và giảm số lượng sản phẩm
        Set<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            OrderItem orderDetails = new OrderItem();
            orderDetails.setOrders(order);
            orderDetails.setProductDetails(cartItem.getProductDetails());
            orderDetails.setQuantity(cartItem.getQuantity());
            orderDetails.setPrice(cartItem.getProductDetails().getSellPrice());
            orderDetails.setStatus(1);

            // Lưu chi tiết đơn hàng vào cơ sở dữ liệu
            orderItemRepository.save(orderDetails);

            // Giảm số lượng sản phẩm trong kho
            reduceProductStock(cartItem.getProductDetails().getId(), cartItem.getQuantity());
        }
    }



    @Override
    public Orders placeOrders(Cart cart, String address, String voucherCode, String selectedVoucherCode) {
        Orders order = createOrder(cart, address);
        if (order == null) {
            return null;
        }

        // Xử lý chi tiết đơn hàng và giảm số lượng sản phẩm
        processOrderDetails(cart, order);

        // Áp dụng voucher nếu có
        applyVoucher(order, voucherCode, selectedVoucherCode);

        // Lưu đơn hàng vào cơ sở dữ liệu
        try {
            order = ordersRepository.save(order);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return order;
    }

    @Transactional
    @Override

    public void reduceProductStock(Integer id, int quantity) {
        Optional<ProductDetails> productDetailOptional = productDetailsRepository.findById(id);

        if (productDetailOptional.isPresent()) {
            ProductDetails productDetail = productDetailOptional.get();

            // Kiểm tra xem có đủ số lượng tồn kho để giảm không
            if (productDetail.getQuantity() >= quantity) {
                int newStock = productDetail.getQuantity() - quantity;
                productDetail.setQuantity(newStock);

                // Lưu thông tin chi tiết sản phẩm với số lượng tồn kho mới vào cơ sở dữ liệu
                productDetailsRepository.save(productDetail);
            } else {
                // Xử lý trường hợp không đủ số lượng tồn kho
                // Có thể báo lỗi, ném exception hoặc thực hiện xử lý khác tùy thuộc vào yêu cầu của ứng dụng
            }
        } else {
            // Xử lý trường hợp không tìm thấy chi tiết sản phẩm
            // Có thể báo lỗi, ném exception hoặc thực hiện xử lý khác tùy thuộc vào yêu cầu của ứng dụng
        }
    }


    @Transactional
    @Override

    public BigDecimal calculateDiscountValue(Voucher voucher, BigDecimal total) {
        if (voucher.isActive() && voucher.getStartDate().isBefore(LocalDateTime.now()) && voucher.getExpiryDateTime().isAfter(LocalDateTime.now())) {
            if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
                BigDecimal discountPercentage = voucher.getDiscount().divide(BigDecimal.valueOf(100));
                return total.multiply(discountPercentage);
            } else {
                return voucher.getDiscount();
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateTotalPriceWithVoucher(String voucherCode, BigDecimal originalTotalPrice) {
        return null;
    }

    @Override
    public void applyVouchers(Orders order, String voucherCode) {

    }
}
