package com.example.datnsd56.rest;

import com.example.datnsd56.entity.SessionCart;
import com.example.datnsd56.service.impl.CartSeviceImpl;
import com.example.datnsd56.service.impl.ProductDetailsServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
@Autowired
private ProductDetailsServiceImpl productDetailsService;
@Autowired
private CartSeviceImpl cartSevice;
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


}
