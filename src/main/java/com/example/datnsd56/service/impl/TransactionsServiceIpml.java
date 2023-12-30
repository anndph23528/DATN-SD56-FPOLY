package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Transactions;
import com.example.datnsd56.repository.TransactionsRepository;
import com.example.datnsd56.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionsServiceIpml implements TransactionService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    public Transactions saveTransaction(Transactions transaction) {

        transactionsRepository.save(transaction);
        return transaction;
    }

    public List<Transactions> get() {
        return  transactionsRepository.findAll();
    }
//
//    @Override
//    public Optional<Transactions> findByOrderId(Integer id) {
//        return transactionsRepository.findByOrderId(id);
//    }

//    @Override
//    public Transactions findByOrderInfo(String orderInfo) {
//        return transactionsRepository.findByOrderInfo(orderInfo);
//    }

    @Override
    public Optional<Transactions> getTransactionsById(Integer id) {
        return transactionsRepository.getTransactionsById(id);
    }

    @Override
    public Transactions getById(Integer id) {
        return transactionsRepository.getReferenceById(id);
    }

    // Các phương thức khác nếu cần
}
