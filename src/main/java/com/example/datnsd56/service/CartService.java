package com.example.datnsd56.service;

import com.example.datnsd56.entity.Cart;
import com.example.datnsd56.entity.CartItem;
import com.example.datnsd56.entity.ProductDetails;
import com.example.datnsd56.entity.SessionCart;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CartService {
    void add(CartItem item);

    void remove(Integer id);

    void clear();

    int getCount();

    void add1(Cart cart);

    CartItem update(Integer id, Integer quantity);

    Collection<CartItem> getAllItem();
//    List<ViewCart> getAllCartItem();

    double getAmount();
//   void addToCart(CartItem cartItem);
 Cart findByNguoiDungId(Integer id);

    Cart addToCart(ProductDetails productDetail, Integer quantity, String name);

    Cart updateCart(ProductDetails productDetail, Integer quantity, String name);

    Cart removeFromCart(ProductDetails productDetail, String name);

    SessionCart addToCartSession(SessionCart sessionCart, ProductDetails productDetail, Integer quantity);

    SessionCart updateCartSession(SessionCart sessionCart, ProductDetails productDetail, Integer quantity);

    SessionCart removeFromCartSession(SessionCart sessionCart, ProductDetails productDetail);

    Cart combineCart(SessionCart sessionCart, String name);

    Cart getCart(String email);

    void deleteCartById(Integer id);

}
