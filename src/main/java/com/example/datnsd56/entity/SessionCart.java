package com.example.datnsd56.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionCart {
    private BigDecimal totalPrice;

    private Integer totalItems;

    private LocalDate createDate;

    private LocalDate updateDate;

    private String status;

    private Set<SessionCartItem> cartItems;

    private Map<Integer, CartItem> cartItemss;

    // Constructors, getters, setters, và các phương thức khác

    public void updateQuantity(Integer productId, Integer newQuantity) {
        if (cartItemss.containsKey(productId)) {
            CartItem cartItem = cartItemss.get(productId);

            // Kiểm tra newQuantity nếu bạn muốn
            if (newQuantity > 0) {
                cartItem.setQuantity(newQuantity);
            } else {
                // Xóa sản phẩm nếu newQuantity là 0 hoặc âm
                cartItems.remove(productId);
            }
        }
    }
}
