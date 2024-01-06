package com.example.datnsd56.rest;

import com.example.datnsd56.entity.Cart;
import com.example.datnsd56.entity.ProductDetails;
import com.example.datnsd56.entity.SessionCart;
import com.example.datnsd56.service.CartService;
import com.example.datnsd56.service.impl.CartSeviceImpl;
import com.example.datnsd56.service.impl.ImageServiceImpl;
import com.example.datnsd56.service.impl.ProductDetailsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @GetMapping("/product/detail/check-quantity")
    @ResponseBody
    public ResponseEntity<Integer> checkQuantity(
            @RequestParam("productId") Integer productId,
            @RequestParam("size") Integer sizeId,
            @RequestParam("color") Integer colorId) {

        // Thực hiện kiểm tra số lượng và trả về số lượng còn lại
        int remainingQuantity = productDetailsService.checkQuantity(productId, colorId, sizeId);

        return new ResponseEntity<>(remainingQuantity, HttpStatus.OK);
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

}
