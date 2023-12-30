package com.example.datnsd56.service;

import com.example.datnsd56.entity.CartItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartDetailService {
    List<CartItem> findAllByIdGioHang(Integer id);

    void deleteById(Integer id);

    CartItem save(CartItem  cartItem);
    Optional<CartItem> findById(Integer id);

    void deleteAllByGioHang_Id(Integer idGioHang);
}
