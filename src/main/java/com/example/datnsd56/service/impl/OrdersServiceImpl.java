
package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.repository.*;
import com.example.datnsd56.security.UserInfoUserDetails;
import com.example.datnsd56.service.CartService;
import com.example.datnsd56.service.OrdersService;
import com.example.datnsd56.service.VoucherService;
import com.example.datnsd56.service.VoucherUsageService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {
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
    private VoucherUsageRepository voucherUsageRepository;
    @Autowired
    private TransactionsServiceIpml transactionsService;
    @Autowired
    private VoucherUsageService voucherUsageService;

    @Autowired
    public void OrdersService(OrdersRepository ordersRepository, TransactionsRepository transactionsRepository) {
        this.ordersRepository = ordersRepository;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public Orders detailHD(Integer id) {
        return ordersRepository.findById(id).orElse(null);
    }

    @Override
    public List<Orders> getAll() {
        return ordersRepository.findAllByOrderStatus(1);
    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
    }

    @Override
    public Page<Orders> getAllOrders(Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createDate"));
        return ordersRepository.findAll(pageable);
    }

    @Override
    public Page<Orders> getAllOrders1(Pageable pageable) {
        return ordersRepository.findAll(pageable);
    }

    @Override
    public Orders getOneBill(Integer id) {
        return ordersRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public Orders update(Orders orders, Integer id) {
        Optional<Orders> optional = ordersRepository.findById(id);
        if (optional.isPresent()) {
            Orders orders1 = optional.get();
            orders.setId(orders1.getId());
            orders.setOrderStatus(orders1.getOrderStatus());
            orders.setCreateDate((orders1.getCreateDate()));
            orders.setUpdateDate((LocalDateTime.now()));
            return ordersRepository.save(orders);
        }
        return null;
    }


    public boolean canUseVoucher(Account account, Voucher voucher) {
        // Kiểm tra xem voucher có được sử dụng bởi tài khoản hay không
        Set<VoucherUsage> voucherUsages = voucher.getVoucherUsages();
        for (VoucherUsage usage : voucherUsages) {
            if (usage.getAccount().equals(account)) {
                return false; // Tài khoản đã sử dụng voucher này
            }
        }
        return true; // Tài khoản chưa sử dụng voucher này
    }

    @Override
    public BigDecimal getNewTotalAfterApplyingVoucher(String username) {
        // Lấy thông tin giỏ hàng của người dùng
        Optional<Account> accountOptional = accountRepository.findByName(username);

        Cart cart = null;
        if (accountOptional.isPresent()) {
            cart = accountOptional.get().getCart();

            // Lấy thông tin voucher đã áp dụng trong giỏ hàng
            Optional<VoucherUsage> appliedVoucherOptional = voucherUsageRepository
                    .findTopByAccountIdAndIsUsedFalseAndVoucher_ActiveTrueOrderByUsedDateDesc(accountOptional.get().getId());

            if (appliedVoucherOptional.isPresent()) {
                VoucherUsage appliedVoucher = appliedVoucherOptional.get();

                // Kiểm tra xem voucher có hợp lệ không
                if (voucherService.canUseVoucher(accountOptional.get(), appliedVoucher.getVoucher())) {
                    // Tính toán giảm giá từ voucher
                    BigDecimal discountValue = calculateDiscountValue(appliedVoucher.getVoucher(), cart.getTotalPrice());

                    // Tính toán giá tiền mới sau khi áp dụng voucher
                    BigDecimal discountedTotal = cart.getTotalPrice().subtract(discountValue);

                    // Kiểm tra xem giảm giá từ voucher có vượt quá tổng giá trị của đơn hàng không
                    if (discountedTotal.compareTo(BigDecimal.ZERO) < 0) {
                        return BigDecimal.ZERO;
                    }

                    return discountedTotal;
                }
            }
        }

        // Nếu không có voucher nào được áp dụng hoặc voucher không hợp lệ, trả về giá tiền hiện tại của giỏ hàng
        return cart.getTotalPrice();
    }


    private BigDecimal calculateTotal(Cart cart, Voucher voucher) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        // Áp dụng giảm giá từ voucher nếu có
        if (voucher != null && voucher.isActive()) {
            if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
                // Giảm giá theo phần trăm
                BigDecimal discountAmount = total.multiply(voucher.getDiscount().divide(BigDecimal.valueOf(100)));
                total = total.subtract(discountAmount);
            } else if (voucher.getDiscountType() == DiscountType.AMOUNT) {
                // Giảm giá cố định
                total = total.subtract(voucher.getDiscount());
            }
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }


    private boolean isVoucherApplicableToOrder(Voucher voucher, Orders order) {
        LocalDateTime currentTime = LocalDateTime.now();

        // Kiểm tra xem voucher có hoạt động không, thời gian hiện tại có trước thời gian hết hạn không
        // và tổng giá trị đơn hàng lớn hơn hoặc bằng giảm giá voucher
        return voucher.isActive() && currentTime.isBefore(voucher.getExpiryDateTime())
                && order.getTotal().compareTo(voucher.getDiscount()) >= 0;
    }

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

    @Autowired
    public OrdersServiceImpl(OrdersRepository ordersRepository, OrderItemRepository orderItemRepository,
                             ProductDetailsRepository productDetailsRepository, CartService cartService) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.productDetailsRepository = productDetailsRepository;
        this.cartService = cartService;
    }


    @Override
    public Orders applyVoucherToOrder(Orders order, Voucher voucher) {
        return null;
    }

    @Override
    public Optional<Orders> getOrderId(Integer id) {
        return ordersRepository.findById(id);
    }

    @Override
    public List<Orders> getNoConfirmOrders(Integer accountId) {
        return ordersRepository.getOrdes(1, accountId);
    }

    @Override
    public List<OrderItem> getLstDetailByOrderId(Integer id) {
        return orderItemRepository.findAllByOrdersId(id);
    }

    public Orders planceOrder(Cart cart, String address) {
        // Khởi tạo và thiết lập giá trị cho Orders
        Orders bill = new Orders();
        bill.setId(1);
        bill.setAddress(address);
        bill.setPhone(cart.getAccountId().getPhone());
        bill.setEmail(cart.getAccountId().getEmail());
        bill.setShippingFee(BigDecimal.ZERO);
        bill.setTotal(BigDecimal.ZERO);
        bill.setOrderStatus(10);
        bill.setCreateDate(LocalDateTime.now());
        bill.setUpdateDate(LocalDateTime.now());
        bill.setAccountId(cart.getAccountId());

        // Lưu Orders để có giá trị 'id' được sinh tự động
        try {
            bill = ordersRepository.save(bill);
        } catch (Exception e) {
            // Xử lý lỗi nếu không thể lưu Orders
            return null;
        }

        // Tạo và lưu OrderItem
        List<OrderItem> billDetailList = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            OrderItem billDetail = new OrderItem();
            billDetail.setOrders(bill);
            billDetail.setProductDetails(item.getProductDetails());
            billDetail.setPrice(item.getPrice());
            billDetail.setQuantity(item.getQuantity());
            billDetail.setStatus(1);

            // Cập nhật và lưu ProductDetails
            ProductDetails productDetail = productDetailsRepository.findById(item.getProductDetails().getId()).orElse(null);
            if (productDetail != null) {
                productDetail.setQuantity(productDetail.getQuantity() - item.getQuantity());
                if (productDetail.getQuantity() == 0) {
                    productDetail.setStatus(true);
                }
                productDetailsRepository.save(productDetail);
            }
            billDetailList.add(billDetail);
        }

        // Thiết lập danh sách OrderItems cho Orders
        bill.setOrderItems(billDetailList);

        // Xóa Cart sau khi tạo đơn hàng thành công
        cartService.deleteCartById(cart.getId());

        return bill;
    }

    //  public Orders planceOrder(Cart cart, String address) {
//        Orders bill = new Orders();
////        Address customerAddress = addressRepository.findById(Integer.valueOf(address)).orElse(null);
//////        bill.setAccountId(customerAddress.getAccount());
////        bill.setAddress(customerAddress.getStreetName() + ", "
////                + customerAddress.getProvince() + ", "
////                + customerAddress.getDistrict() + ", "
////                + customerAddress.getZipcode());
//        bill.setAddress(address);
//        bill.setPhone(cart.getAccountId().getPhone());
//        bill.setEmail(cart.getAccountId().getEmail());
//        bill.setShippingFee(BigDecimal.ZERO);
//        bill.setTotal(BigDecimal.ZERO);
//        bill.setOrderStatus(1);
//        bill.setCreateDate(LocalDate.now());
//        bill.setUpdateDate(LocalDate.now());
//        bill.setAccountId(cart.getAccountId());
//
//        List<OrderItem> billDetailList = new ArrayList<>();
//        for (CartItem item : cart.getCartItems()){
//            OrderItem billDetail = new OrderItem();
//            billDetail.setOrders(bill);
//            billDetail.setProductDetails(item.getProductDetails());
//            billDetail.setPrice(item.getPrice());
//            billDetail.setQuantity(item.getQuantity());
//            billDetail.setStatus("1");
//            orderItemRepository.save(billDetail);
//            billDetailList.add(billDetail);
//            ProductDetails productDetail = productDetailsRepository.findById(item.getProductDetails().getId()).orElse(null);
//            productDetail.setQuantity(productDetail.getQuantity() - item.getQuantity());
//            if (productDetail.getQuantity() == 0){
//                productDetail.setStatus(true);
//            }
//            productDetailsRepository.save(productDetail);
//        }
//
//        bill.setOrderItems(billDetailList);
//        cartService.deleteCartById(cart.getId());
//        return ordersRepository.save(bill);
//    }
    @Override
    public Orders add(Orders hoaDon) {
        hoaDon.setOrderStatus(10);
        hoaDon.setCreateDate(LocalDateTime.now());
        hoaDon.setUpdateDate(LocalDateTime.now());
        return ordersRepository.save(hoaDon);
    }

    @Override
    public Orders shippingOrder(Integer id, BigDecimal shippingFee) {
        Orders bill = ordersRepository.findById(id).orElse(null);
        bill.setOrderStatus(2);
        bill.setShippingFee(shippingFee);
        bill.setTotal(bill.getTotal().add(shippingFee));
        return ordersRepository.save(bill);
    }

    @Override
    public Orders completeOrder(Integer id,Account account) {
        Orders bill = ordersRepository.findById(id).orElse(null);
//            bill.setOrderStatus(1);
//            bill.setCreateDate(LocalDateTime.now());
//        return ordersRepository.save(bill);
        if (bill.getOrderStatus() != 2) {
            return ordersRepository.save(bill);
        } else {
            bill.setOrderStatus(1);
            bill.setCreateDate(LocalDateTime.now());
            bill.setAccountId(account);
            List<Transactions> listPaymentMethod = transactionsRepository.findAllByOrderId(id);
            for (Transactions paymentMethod : listPaymentMethod) {
                if ("pending".equals(paymentMethod.getStatus())) {
                    paymentMethod.setStatus("success");
                    transactionsRepository.save(paymentMethod);
                }
            }
            return ordersRepository.save(bill);
        }
    }

    @Override
    public Page<Orders> filterAndSearch(LocalDate startDate, LocalDate endDate,String searchInput, Integer page) {
        // Create a Pageable object for the repository query
        Pageable pageable = PageRequest.of(page, 90, Sort.by(Sort.Direction.DESC, "createDate"));

        // Get the unfiltered page from the repository
        Page<Orders> historyList = ordersRepository.findAll(pageable);

        // Lọc theo ngày
        if (startDate != null && endDate != null) {
            List<Orders> filteredList = historyList.getContent()
                    .stream()
                    .filter(history -> !history.getCreateDate().isBefore(startDate.atStartOfDay())
                            && !history.getCreateDate().isAfter(endDate.atStartOfDay().plusDays(1)))
                    .collect(Collectors.toList());

            // Create a new PageImpl with the filtered list
            return new PageImpl<>(filteredList, pageable, filteredList.size());
        }

        // If no date filtering is needed, return the unfiltered page
        return historyList;
    }



    @Override
    public Page<Orders> findByPhone(String phone) {
        Pageable page=PageRequest.of(0,10);
        Page<Orders> list= ordersRepository.findOrdersByPhone(phone,page);
        return list;
    }

    //    @Override
//    public Page<Orders> search(LocalDateTime createDate, LocalDateTime updateDate) {
//        Pageable pageable = PageRequest.of(0, 10);
//        return ordersRepository.searchOrder( createDate,updateDate, pageable);
//    }
    @Override
    @Transactional
    public Orders cancelOrder(Integer id, Account account) {
        Orders bill = ordersRepository.findById(id).orElse(null);

        if (bill != null && bill.getOrderStatus() == 0) {
            return ordersRepository.save(bill);
        } else if (bill != null) {
            bill.setOrderStatus(0);
            bill.setAccountId(account);
            List<Transactions> paymentMethodList = transactionsRepository.findAllByOrderId(id);
            for (Transactions paymentMethod : paymentMethodList) {
                paymentMethod.setStatus("fail");
                transactionsRepository.save(paymentMethod);
            }

            List<OrderItem> billDetailList = new ArrayList<>(bill.getOrderItems());
            for (OrderItem billDetail : billDetailList) {
                billDetail.setStatus(0);
                orderItemRepository.save(billDetail);

                ProductDetails productDetail = productDetailsRepository.findById(billDetail.getProductDetails().getId()).orElse(null);
                if (productDetail != null) {
                    productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
                    productDetail.setStatus(true);
                    productDetailsRepository.save(productDetail);
                }
            }

            bill.setOrderItems(billDetailList);
            return ordersRepository.save(bill);
        }

        return null; // Trả về null nếu đơn hàng không tồn tại
    }



    @Override
    public Orders acceptBill(Integer Id) {
        Orders bill = ordersRepository.findById(Id).orElse(null);
        bill.setOrderStatus(3);
//        bill.setCreateDate(LocalDate.now());
//        bill.setUpdateDate(LocalDate.now());
        return ordersRepository.save(bill);
    }


    @Override
    public List<Orders> getAllOrders1(Integer accountId) {
        return ordersRepository.getAllOrders(accountId);
    }
}


