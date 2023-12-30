package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.CartItem;
import com.example.datnsd56.repository.CartItemRepository;
import com.example.datnsd56.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemServiceImpl implements CartDetailService {
    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public List<CartItem> findAllByIdGioHang(Integer id) {
        return cartItemRepository.findAllByCartId(id);
    }

    @Override
    public void deleteById(Integer id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public Optional<CartItem> findById(Integer id) {
        Optional<CartItem> chiTietGioHang = cartItemRepository.findById(id);

        if (chiTietGioHang.isPresent()){
            return chiTietGioHang;
        }
        return Optional.empty();
    }

    @Override
    public void deleteAllByGioHang_Id(Integer idGioHang) {
       cartItemRepository.deleteAllByCartId(idGioHang);
    }
}
