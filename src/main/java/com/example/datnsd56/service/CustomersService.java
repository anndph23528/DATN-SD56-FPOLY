package com.example.datnsd56.service;

import com.example.datnsd56.entity.Customers;
import com.example.datnsd56.entity.Customers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomersService {
    Page<Customers> getAll(Pageable pageable);
    Customers detail(Integer id);
    Customers add(Customers customers);
    void update(Customers customers);
    void delete(Integer id);
    Page<Customers> findByEmail(String phone);
    List<Customers> get();

}
