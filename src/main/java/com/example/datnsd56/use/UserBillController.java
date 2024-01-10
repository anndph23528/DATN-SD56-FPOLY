package com.example.datnsd56.use;


import com.example.datnsd56.entity.*;
import com.example.datnsd56.repository.AddressRepository;
import com.example.datnsd56.repository.OrdersRepository;
import com.example.datnsd56.repository.VoucherUsageRepository;
import com.example.datnsd56.service.*;

import com.example.datnsd56.service.impl.OrderServiceImplV2;
import com.example.datnsd56.service.impl.PaymentServiceImpl;
import com.example.datnsd56.service.impl.VoucherSeviceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@SessionAttributes("appliedVoucherCode")
@Controller
@RequestMapping("/user")
public class

UserBillController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private AddressService service;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private VoucherUsageRepository voucherUsageRepository;
    @Autowired
    private CartService cartServicel;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentServiceImpl paymentService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private VoucherUsageService voucherUsageService;
    @Autowired
    private OrderSeriveV2 orderServiceImplV2;
    @Autowired
    private OrderServiceImplV2 orderServiceImplV21;
    @Autowired
    private OrderServiceImplV2 odserv2;
    @Autowired
    private OrdersRepository ordersRepository;
    //    @Autowired
//    private VnpayUtils vnpayUtils;

    @GetMapping("/checkout")
    public String checkout(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Account> accountOptional = accountService.finByName(principal.getName());
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Cart cart = account.getCart();

            // Tìm địa chỉ mặc định
            List<Address> addressList = addressService.findAccountAddresses(account.getId());
            // Lấy danh sách tất cả voucher
            List<Voucher> allVouchers = voucherService.getAllls();

            // Lấy danh sách voucher đã lưu cho tài khoản
            List<VoucherUsage> voucherUsages = voucherUsageService.findVisibleVoucherUsagesByAccount(accountOptional.get().getId());

            // Loại bỏ những voucher đã lưu khỏi danh sách tất cả voucher
            allVouchers.removeAll(voucherUsages.stream().map(VoucherUsage::getVoucher).collect(Collectors.toList()));

//        model.addAttribute("allVouchers", allVouchers);
            model.addAttribute("voucherUsages", voucherUsages);
            if (addressList.isEmpty()) {
                // Nếu không có địa chỉ, hiển thị trang giỏ hàng
                model.addAttribute("cart", cart);
                model.addAttribute("addressList", addressList);
                model.addAttribute("newAddress", new Address()); // Thêm đối tượng mới cho modal
                model.addAttribute("defaultAddress", null); // Không có địa chỉ mặc định
                return "website/index/giohang1";
            } else {
                Address defaultAddress = addressList.stream()
                    .filter(address -> Boolean.TRUE.equals(address.getDefaultAddress()))
                    .findFirst()
                    .orElse(null);

                model.addAttribute("cart", cart);
                model.addAttribute("addressList", addressList);
                model.addAttribute("newAddress", new Address()); // Thêm đối tượng mới cho modal
                model.addAttribute("defaultAddress", defaultAddress); // Địa chỉ mặc định
                return "website/index/giohang1";
            }

        }
        return "redirect:/login";
    }

    @PostMapping("/add-order")
    public String placeOrder(@RequestParam(name = "selectedAddressRadio", required = false) Integer selectedAddressId,
                             @RequestParam(name = "paymentMethod") String paymentMethod,
                             @RequestParam(name = "promoCode", required = false) String voucherCode,
                             @RequestParam(name = "selectedVoucherCode", required = false) String selectedVoucherCode,
                             Principal principal,
                             RedirectAttributes attributes,
                             HttpSession session,
                             HttpServletRequest request,
                             Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Account> accountOptional = accountService.finByName(principal.getName());
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Cart cart = account.getCart();

            // Kiểm tra xem người dùng đã chọn địa chỉ từ danh sách hay không
            Address address;
            if (selectedAddressId != null) {
                // Sử dụng địa chỉ từ danh sách
                Optional<Address> selectedAddressOptional = addressService.findAccountAddresses(account.getId())
                    .stream()
                    .filter(addr -> addr.getId().equals(selectedAddressId))
                    .findFirst();
                address = selectedAddressOptional.orElse(null);
            } else {
                // Lấy địa chỉ mặc định
                address = addressService.findDefaultAddress(account.getId());
            }

            if (address != null) {
                // Thực hiện đặt hàng
                BigDecimal appliedVoucherTotal = (BigDecimal) session.getAttribute("appliedVoucherTotal");

                if ("vnpay".equals(paymentMethod)) {
                    // Tạo và lưu thông tin thanh toán vào bảng Transactions
                    Orders order = orderServiceImplV21.placeOrderss(cart, String.valueOf(address), voucherCode, selectedVoucherCode);

                    // Lưu đơn hàng vào cơ sở dữ liệu
                    if (order != null) {
                        Transactions transaction = new Transactions();
                        transaction.setAmount(order.getTotal());
                        transaction.setCreateDate(LocalDate.now());
                        transaction.setUpdateDate(LocalDate.now());
                        transaction.setPaymentMethod(paymentMethod);
                        transaction.setStatus("pending");
                        transaction.setCustomerId(order.getCustomerId());
                        transaction.setOrderId(order);
                        transaction.setAccountId(account.getCart().getAccountId());

                        // Lưu giao dịch tạm thời vào cơ sở dữ liệu và giữ ID
                        Transactions savedTransaction = transactionService.saveTransaction(transaction);

                        // Lưu ID của giao dịch vào session để sử dụng sau này
                        session.setAttribute("pendingTransactionId", savedTransaction.getId());

                        // Tạo URL thanh toán VNPAY và chuyển hướng đến trang thanh toán
                        String vnpPaymentUrl = paymentService.createVnpPaymentUrl(order.getTotal(), order.getId(), savedTransaction.getId());
                        return "redirect:" + vnpPaymentUrl;
                    } else {
                        attributes.addFlashAttribute("error", "Đặt hàng không thành công. Vui lòng thử lại sau!");
                        return "redirect:/user/checkout";
                    }
                }

                // Thực hiện đặt hàng
                Orders order = orderServiceImplV2.placeOrder(cart, String.valueOf(address), voucherCode, selectedVoucherCode);

                // Kiểm tra xem đã áp dụng voucher chưa và lưu giá mới vào hóa đơn
                if (appliedVoucherTotal != null && order != null) {
                    order.setTotal(appliedVoucherTotal);
                    ordersService.add(order);
                }

                if (order != null) {
//                    Orders orders = orderServiceImplV2.placeOrder(cart, String.valueOf(address), voucherCode, selectedVoucherCode);

                    // Kiểm tra xem phương thức thanh toán là cashOnDelivery hay không
                    if ("cashOnDelivery".equals(paymentMethod)) {
                        // Tạo và lưu thông tin thanh toán vào bảng Transactions
                        Transactions transaction = new Transactions();
                        transaction.setAmount(order.getTotal());
                        transaction.setCreateDate(LocalDate.now());
                        transaction.setUpdateDate(LocalDate.now());
                        transaction.setPaymentMethod(paymentMethod);
                        transaction.setStatus("pending"); // Điều chỉnh trạng thái thành công
                        transaction.setCustomerId(order.getCustomerId());
                        transaction.setOrderId(order);
                        transaction.setAccountId(account.getCart().getAccountId());
                        transactionService.saveTransaction(transaction);
                        session.removeAttribute("appliedVoucherTotal");

                        // Xóa giỏ hàng
                        cartServicel.deleteCartById(cart.getId());
                        return "redirect:/user/transaction-success/" + order.getId();
                    }

                    // Các logic xử lý khác, ví dụ lưu đơn hàng và xóa giỏ hàng
                    // ...

                    // Hiển thị trang thông tin giao dịch thành công
                    attributes.addFlashAttribute("order", order);
                    return "redirect:/user/transaction-success/" + order.getId();
                } else {
                    attributes.addFlashAttribute("error", "Đặt hàng không thành công. Vui lòng thử lại sau!");
                }
            } else {
                attributes.addFlashAttribute("error", "Không tìm thấy địa chỉ. Vui lòng thử lại sau!");
            }
        } else {
            return "redirect:/login";
        }

        return "redirect:/user/checkout";
    }
    @GetMapping("/vnpay-ipn")
    public String vnpayIPN(@RequestParam("vnp_ResponseCode") String responseCode,
                           HttpServletRequest request,
                           Model model, Principal principal,
                           HttpSession session) {
        // Xử lý phản hồi từ VNP và hiển thị thông tin giao dịch
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");

        // Kiểm tra xem có ID của giao dịch trong session không
        Integer transactionId = (Integer) session.getAttribute("pendingTransactionId");

        if (transactionId != null) {
            // Lấy giao dịch tạm thời từ cơ sở dữ liệu
            Optional<Transactions> transactionOptional = transactionService.getTransactionsById(transactionId);

            Optional<Account> accountOptional = accountService.finByName(principal.getName());
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                Cart cart = account.getCart();

                // Kiểm tra xem người dùng đã chọn địa chỉ từ danh sách hay không
                if (transactionOptional.isPresent()) {
                    Transactions pendingTransaction = transactionOptional.get();

                        // Lưu chi tiết đơn hàng vào cơ sở dữ liệu
//                        orderItemRepository.save(orderDetails);

                        // Giảm số lượng sản phẩm trong kho
//                        reduceProductStock(cartItem.getProductDetails().getId(), cartItem.getQuantity());
//                    orderServiceImplV21.reduceProductStock(cartItem.getProductDetails().getId(), cartItem.getQuantity());

                    if ("00".equals(responseCode)) {
                        // Cập nhật thông tin giao dịch và đổi trạng thái thành "success"
                        pendingTransaction.setOrderInfo(vnp_TxnRef);
                        pendingTransaction.setStatus("success");
                        transactionService.saveTransaction(pendingTransaction);
                        session.removeAttribute("appliedVoucherTotal");
                        Set<CartItem> cartItems = cart.getCartItems();
                        for (CartItem cartItem : cartItems) {
                            OrderItem orderDetails = new OrderItem();
                            orderDetails.setOrders(transactionOptional.get().getOrderId());
                            orderDetails.setProductDetails(cartItem.getProductDetails());
                            orderDetails.setQuantity(cartItem.getQuantity());
                            orderDetails.setPrice(cartItem.getProductDetails().getSellPrice());
                            orderDetails.setStatus(1);
                            orderServiceImplV21.reduceProductStock(cartItem.getProductDetails().getId(), cartItem.getQuantity());

                        }
                        cartServicel.deleteCartById(cart.getId());
                        model.addAttribute("tr", pendingTransaction);
                        // ... (Thêm các thuộc tính khác cần thiết)
                        return "website/index/payment-result";

                    } else {
                        // Xử lý khi thanh toán không thành công
//                        pendingTransaction.setOrderInfo(vnp_TxnRef);
                        pendingTransaction.setStatus("fail");
                        Optional<Orders> orderOptional = ordersRepository.findById(transactionOptional.get().getOrderId().getId());

                        if (orderOptional.isPresent()) {
                            Orders orderToUpdate = orderOptional.get();

                            // Cập nhật trạng thái của hóa đơn thành 0
                            orderToUpdate.setOrderStatus(0);

                            // Lưu lại đối tượng Orders đã cập nhật vào cơ sở dữ liệu
                            ordersRepository.save(orderToUpdate);
                            transactionService.saveTransaction(pendingTransaction);

                            return "redirect:/user/checkout";

                            // ... (Thêm các thuộc tính khác cần thiết)
                        }

                        // Lưu thông tin giao dịch vào cơ sở dữ liệu
                        transactionService.saveTransaction(pendingTransaction);

                        // Xóa ID của giao dịch khỏi session
                        session.removeAttribute("pendingTransactionId");

                        // Gọi view để hiển thị thông tin giao dịch
                        return "website/index/f";
                    }
                }

                // Xử lý khi không tìm thấy thông tin giao dịch tạm thời hoặc gặp lỗi khác
                return "website/index/payment-failure";
            }
            // Hiển thị thông tin giao dịch trên trang
            return null;
        }
        return null;
    }
    @PostMapping("/apply-voucher")
    public ResponseEntity<Map<String, Object>> applyVoucher(
        @RequestParam(name = "selectedVoucherCode", required = false) String selectedVoucherCode,
        Principal principal, HttpSession session, Model model) {

        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Account> accountOptional = accountService.finByName(principal.getName());
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Cart cart = account.getCart();

            if (cart != null) {
                try {
                    // Tính toán giá tạm thời khi áp dụng voucher
                    BigDecimal newTotal = orderServiceImplV2.calculateTotalWithVoucher(cart, selectedVoucherCode, principal.getName());

                    // Lưu giá mới vào session để sử dụng khi đặt hàng
                    session.setAttribute("appliedVoucherTotal", newTotal);

                    // Thiết lập thông điệp thành công trong Model để truyền về view
                    model.addAttribute("ss", "Áp dụng mã giảm giá thành công");

                    // Trả về phản hồi JSON với giá mới để cập nhật giao diện người dùng
                    Map<String, Object> response = new HashMap<>();
                    response.put("newTotal", newTotal.toString());
                    response.put("ss", "Áp dụng mã giảm giá thành công");
                    return ResponseEntity.ok(response);
                } catch (RuntimeException e) {
                    // Xử lý ngoại lệ ném từ service
                    Map<String, Object> response = new HashMap<>();
                    response.put("ss1", e.getMessage());
                    return ResponseEntity.badRequest().body(response);
                }
            }
        }

        // Trả về phản hồi lỗi nếu có vấn đề
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/setDefaultAddress")
    public String setDefaultAddress(@RequestParam("addressId") Integer addressId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Account> accountOptional = accountService.finByName(principal.getName());
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            // Đặt địa chỉ có addressId làm địa chỉ mặc định cho tài khoản
            addressService.setDefaultAddress(account, addressId);

        }

        return "redirect:/user/checkout";
    }


    // Các phương thức khác...
    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";

        }

        Optional<Account> account = accountService.finByName(principal.getName());
        List<Orders> listOrder = ordersService.getAllOrders1(account.get().getId());
        model.addAttribute("orders", listOrder);

        return "website/index/danhsachdonhang";
    }

    @GetMapping("/order-detail/{id}")
    public String getOrderDetail(@PathVariable Integer id, Model model, Principal principal, RedirectAttributes attributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        Optional<Account> account = accountService.finByName(principal.getName());
        Orders bill = ordersService.getOneBill(id);
        if (bill == null) {
            attributes.addFlashAttribute("success", "Không có thông tin đơn hàng tương ứng");
            return "redirect:/user/orders";
        }
        if (account.get().getId().equals(bill.getAccountId().getId())) {
            List<OrderItem> lstBillDetails = ordersService.getLstDetailByOrderId(id);
            List<Transactions> listTransactiopn = transactionService.findAllByOrderId(id);
            model.addAttribute("bill", bill);
            model.addAttribute("lstBillDetails", lstBillDetails);
            model.addAttribute("listTransaction", listTransactiopn);
            return "website/index/danhsachdonhangdetail";
        } else {
            attributes.addFlashAttribute("success", "Bạn không có quyền xem đơn hàng này");
            return "redirect:/user/orders";
        }
    }


    @GetMapping("/transaction-success/{orderId}")
    public String transactionSuccess(@PathVariable("orderId") Integer orderId, Model model) {
        // Lấy thông tin đơn hàng từ orderId và đưa vào model để hiển thị
        Optional<Orders> orderOptional = ordersService.getOrderId(orderId);

        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();
            model.addAttribute("order", order);

            // Chuyển đến trang thông tin giao dịch thành công
            return "/website/index/giaodich";
        } else {
            // Đơn hàng không tồn tại, có thể xử lý thông báo lỗi hoặc chuyển hướng đến trang lỗi khác
            return "redirect:/error";
        }
    }

    @PostMapping("/add1")
    public ModelAndView add1(@Valid @ModelAttribute("newAddress") Address newAddress,
                             BindingResult result, HttpSession session, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            // Nếu có lỗi, chuyển về trang thanh toán với model chứa thông tin giỏ hàng và danh sách địa chỉ
            Optional<Account> accountOptional = accountService.finByName(principal.getName());
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                Cart cart = account.getCart();
                modelAndView.addObject("cart", cart);

                List<Address> accountAddresses = addressService.findAccountAddresses(account.getId());
                modelAndView.addObject("accountAddresses", accountAddresses);

                modelAndView.setViewName("website/index/giohang1");
            } else {
                modelAndView.setViewName("redirect:/login");
            }
        } else {
            Optional<Account> accountOptional = accountService.finByName(principal.getName());
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                // Kiểm tra xem có địa chỉ được chọn từ danh sách không
                if (newAddress.getId() != null) {
                    Address selectedAddress = service.findAccountDefaultAddress(newAddress.getId());
                    // Thực hiện đặt hàng với địa chỉ đã chọn
                    // ...

                } else {
                    // Nếu không có địa chỉ được chọn, sử dụng địa chỉ mới từ form
                    Address savedAddress = addressService.addNewAddress(account, newAddress, newAddress.getDefaultAddress());

                    // Thực hiện đặt hàng với địa chỉ mới
                    // ...

                    session.setAttribute("successMessage", "Thêm thành công");
                    modelAndView.setViewName("redirect:/user/checkout");
                }
            } else {
                modelAndView.setViewName("redirect:/login");
            }
        }

        return modelAndView;
    }

    @PostMapping("/update1/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ModelAndView update(@Valid @ModelAttribute("newAddress") Address newAddress,
                               Principal principal,
                               BindingResult result,
                               @PathVariable("id") Integer id,
                               Model model,
                               HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            // Handle validation errors...
            Optional<Account> accountOptional = accountService.finByName(principal.getName());
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                Cart cart = account.getCart();
                modelAndView.addObject("cart", cart);

                List<Address> accountAddresses = addressService.findAccountAddresses(account.getId());
                modelAndView.addObject("accountAddresses", accountAddresses);

                modelAndView.setViewName("website/index/giohang1");
            } else {
                modelAndView.setViewName("redirect:/login");
            }
        } else {
            Optional<Account> accountOptional = accountService.finByName(principal.getName());
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                // Kiểm tra xem có địa chỉ được chọn từ danh sách không
                if (newAddress.getId() != null) {
                    // Thực hiện đặt hàng với địa chỉ đã chọn
                    Address selectedAddress = addressService.findAccountDefaultAddress(newAddress.getId());
                    // Thực hiện các bước đặt hàng với địa chỉ đã chọn
                    // ...

                } else {
                    // Nếu không có địa chỉ được chọn, sử dụng địa chỉ mới từ form
                    Address savedAddress = addressService.addNewAddress(account, newAddress, newAddress.getDefaultAddress());

                    // Thực hiện các bước đặt hàng với địa chỉ mới
                    // ...

                    session.setAttribute("successMessage", "Thêm thành công");
                    modelAndView.setViewName("redirect:/user/checkout");
                }
            } else {
                modelAndView.setViewName("redirect:/login");
            }
        }

        return modelAndView;
    }




    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable Integer id, RedirectAttributes attributes, Principal principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//        Optional<Account> account = accountService.finByName(principal.getName());
//        ordersService.cancelOrder(id,account.get());
//        attributes.addFlashAttribute("success", "Huỷ đơn hàng thành công!");
//        return "redirect:/user/orders";
        Orders bill = ordersService.getOneBill(id);
        String name = principal.getName();
        Optional<Account> account = accountService.finByName(name);
        if (bill != null){
            ordersService.cancelOrder(id, account.get());
            attributes.addFlashAttribute("success", "Huỷ đơn hàng thành công!");
        }
        return "redirect:/user/orders";
    }

}

//    @GetMapping("/dsdonhang")
//    public String viewdsdonhang() {
//
//        return "website/index/danhsachdonhang";
//    }


