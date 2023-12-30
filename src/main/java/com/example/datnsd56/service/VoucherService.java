package com.example.datnsd56.service;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VoucherService {
    Page<Voucher> getAll(Integer page);
    Page<Voucher> getAllbypad(Pageable pageable);
    Page<Voucher> getAll(Pageable pageable);
    Optional<Voucher> findByCode(String code);
    void updateVoucherStatus(Voucher voucher);
List<Voucher> get();
    Voucher detail(Integer id);
    Voucher add(Voucher voucher);
    void update(Voucher voucher);
    void delete(Integer id);
    List<Voucher> getAllls();

}
