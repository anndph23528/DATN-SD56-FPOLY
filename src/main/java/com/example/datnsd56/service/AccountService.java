package com.example.datnsd56.service;

import com.example.datnsd56.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Page<Account> getAll(Pageable pageable);
    Account detail(Integer id);
    Account add(Account account);
    void update(Account account);
    void delete(Integer id);
    Page<Account> findByPhone(String phone);
    List<Account> get();
    Optional<Account> findById(Integer id);
    Optional<Account> finByName(String username);
    List<Account> findByEmail1(String email);
    Account findByEmail(String Email);


}
