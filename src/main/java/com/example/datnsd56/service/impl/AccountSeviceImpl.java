package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.repository.AccountRepository;
import com.example.datnsd56.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountSeviceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Page<Account> getAll(Pageable pageable) {
//        Pageable pageable = PageRequest.of(page, 5);
        return accountRepository.findAll(pageable);
    }

    @Override
    public Account detail(Integer id) {
        Account account = accountRepository.findById(id).orElse(null);
        return account;
    }
//
    @Override
    public Account add(Account account) {
        account.setCreateDate(new Date());
        account.setUpdateDate(new Date());
        return  accountRepository.save(account);
    }

    @Override
    public void update(Account account) {
        account.setCreateDate(new Date());
        account.setUpdateDate(new Date());
        accountRepository.save(account);

    }

    @Override
    public void delete(Integer id) {
        Account account = detail(id);
        accountRepository.delete(account);
    }
    public Page<Account> findByPhone(String phone) {
        Pageable page=PageRequest.of(0,5);
    Page<Account> list=accountRepository.findAccountByPhone(phone,page);
      return list;
    }

    @Override
    public List<Account> get() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(Integer id) {
        Optional<Account> nguoiDung = accountRepository.findById(id);

        if (nguoiDung.isPresent()){
            return nguoiDung;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> finByName(String username) {
        Optional<Account> nguoiDung = accountRepository.findByName(username);

        if (nguoiDung.isPresent()){
            return nguoiDung;
        }
        return Optional.empty();
    }

    @Override
    public  List<Account> findByEmail1(String email){

        return accountRepository.findByEmail1(email);
    }

    @Override
    public Account findByEmail(String Email) {
        return accountRepository.findByEmail(Email);
    }


}
