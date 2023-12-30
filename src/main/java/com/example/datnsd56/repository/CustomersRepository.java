package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Address;
import com.example.datnsd56.entity.Customers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomersRepository extends JpaRepository<Customers,Integer> {
    @Query(value = "SELECT * FROM Customers WHERE phone = ?1 or fullname = ?1",
        nativeQuery = true)
    Page<Customers> findAddressesByPhone(String phone, Pageable pageable);
}
