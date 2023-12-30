package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<Transactions,Integer> {
    Optional<Transactions> getTransactionsById(Integer id);

    @Query(value = "select * from Transactions where order_info=?1",nativeQuery = true)
    Transactions findByOrderInfo(String orderInfo);
//    Optional<Transactions> findByOrderId(Integer id);

}
