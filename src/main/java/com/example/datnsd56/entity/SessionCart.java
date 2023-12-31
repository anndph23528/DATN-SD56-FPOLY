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
    private Integer id;

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

    public int getTotalProducts() {
        int total = 0;
        for (SessionCartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    public int getQuantity(Integer productId, Integer colorId, Integer sizeId) {
       CartItem cartItem=new CartItem();
        for (CartItem cartItems : cartItemss.values()) {
            if (cartItem.matches(productId, colorId, sizeId)) {
                return cartItems.getQuantity();
            }
        }
        return 0; // Trả về 0 nếu không tìm thấy sản phẩm trong giỏ hàng
    }
}
