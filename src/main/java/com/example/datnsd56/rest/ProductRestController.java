package com.example.datnsd56.rest;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.service.CartService;
import com.example.datnsd56.service.impl.CartSeviceImpl;
import com.example.datnsd56.service.impl.ImageServiceImpl;
import com.example.datnsd56.service.impl.ProductDetailsServiceImpl;
import com.example.datnsd56.service.impl.VoucherSeviceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProductRestController {
@Autowired
private ProductDetailsServiceImpl productDetailsService;
@Autowired
private CartSeviceImpl cartSevice;
    @Autowired
    private CartService cartServices;
    @Autowired
    private ImageServiceImpl imageService;
    @Autowired
    private VoucherSeviceImpl voucherService;
//    @Autowired
//    private RateService rateService;
@GetMapping("/product/detail/check-quantity")
@ResponseBody
public ResponseEntity<Integer> checkQuantity(
    @RequestParam("productId") Integer productId,
    @RequestParam("size") Integer sizeId,
    @RequestParam("color") Integer colorId,
    @RequestParam("quantity") Integer quantity) {

    // Thực hiện kiểm tra số lượng và trả về số lượng còn lại
    int remainingQuantity = productDetailsService.checkQuantity(productId, colorId, sizeId, quantity);

    return new ResponseEntity<>(remainingQuantity, HttpStatus.OK);
}
    @PostMapping("/admin/voucher/new")
    @ResponseBody
    public ResponseEntity<String> newVoucherSubmit(@ModelAttribute Voucher voucher, Model model, HttpSession session) {
        BigDecimal discount = voucher.getDiscount();

        if (discount != null) {
            if (voucher.getDiscountType() == DiscountType.PERCENTAGE &&
                (discount.compareTo(BigDecimal.valueOf(80)) >= 0 &&
                    discount.compareTo(BigDecimal.valueOf(100)) <= 0)) {
                // Trả về thông báo yêu cầu xác nhận
                return ResponseEntity.ok("confirm");
            } else {
                // Thực hiện lưu voucher
                voucher.setStartDate(LocalDateTime.now());
                voucher.setActive(true);
                voucher.setDiscount(discount.setScale(2, RoundingMode.HALF_UP)); // Set scale cho giá trị discount
                voucherService.saveVoucher(voucher);
                session.setAttribute("successMessage", "Voucher created successfully!");
                return ResponseEntity.ok("success");
            }
        } else {
            // Xử lý khi discount là null, có thể trả về thông báo lỗi hoặc thực hiện các xử lý khác
            return ResponseEntity.ok("error");
        }
    }



    @GetMapping("/product/cart-total-items")
    @ResponseBody
    public ResponseEntity<Integer> getCartTotalItems(HttpSession session) {
        SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");

        if (sessionCart != null) {
            int totalItems = cartSevice.calculateTotalItems(sessionCart);
            return new ResponseEntity<>(totalItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }
    @PostMapping("/product/update-cart")
    public ResponseEntity<String> updateCart(@RequestParam("productId") Integer productId,
                                             @RequestParam("quantity") Integer quantity,
                                             Principal principal,
                                             HttpSession session) {
        try {
            // Lấy thông tin sản phẩm từ cơ sở dữ liệu
            Optional<ProductDetails> productDetailsOptional = productDetailsService.findById(productId);

            if (productDetailsOptional.isEmpty()) {
                return ResponseEntity.status(404).body("Không tìm thấy sản phẩm");
            }

            ProductDetails productDetails = productDetailsOptional.get();

            // Kiểm tra xem số lượng nhập vào có lớn hơn số lượng tồn kho không
            if (quantity > productDetails.getQuantity() || quantity <=0) {
                return ResponseEntity.status(400).body("Số lượng nhập vào vượt quá số lượng tồn kho");
            }

            // Kiểm tra và cập nhật số lượng trong giỏ hàng
            if (principal == null) {
                SessionCart oldSessionCart = (SessionCart) session.getAttribute("sessionCart");
                SessionCart sessionCart = cartServices.updateCartSession(oldSessionCart, productDetails, quantity);
                session.setAttribute("sessionCart", sessionCart);
                session.setAttribute("totalItems", sessionCart.getTotalItems());
            } else {
                String name = principal.getName();
                Cart cart = cartServices.updateCart(productDetails, quantity, name);
                session.setAttribute("totalItems", cart.getTotalItems());
            }

            // Trả về phản hồi JSON thành công
            return ResponseEntity.ok().body("Cập nhật số lượng thành công");
        } catch (Exception e) {
            // Trả về phản hồi JSON thất bại
            return ResponseEntity.status(500).body("Cập nhật số lượng thất bại");
        }
    }
    @GetMapping("/product/check-login-status")
    @Transactional
    @ResponseBody
    public Map<String, Boolean> checkLoginStatus(Principal principal) {
        boolean isLoggedIn = principal != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("loggedIn", isLoggedIn);
        return response;
    }
    @DeleteMapping("/product/delete-image/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Integer imageId) {
        try {
            imageService.deleteImage(imageId);
            return ResponseEntity.ok("{\"success\": true}");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Ảnh không tồn tại.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Xóa ảnh thất bại.\"}");
        }
    }
//    @PostMapping("/product/submit")
//    public ResponseEntity<String> submitReview(@RequestParam("orderItemId") Integer orderItemId,
//                                               @RequestParam Integer accountId,
//                                               @RequestParam Integer rating,
//                                               @RequestParam String comment) {
//        try {
//            rateService.addRate(orderItemId, accountId, rating, comment);
//            return ResponseEntity.ok("Đánh giá đã được gửi thành công!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi khi xử lý đánh giá.");
//        }
//    }

}
