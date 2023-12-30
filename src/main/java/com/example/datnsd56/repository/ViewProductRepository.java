package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewProductRepository extends JpaRepository<ProductDetails,Integer> {
//    @Query("SELECT a FROM Account a WHERE like :a.phone")
//    List<Account> findAccountByPhone(String phone);
//@Query(value = "SELECT * FROM Account WHERE phone = ?1 or email = ?1",
//    nativeQuery = true)
//
//Page<Account> findAccountByPhone(String phone, Pageable pageable);
}

