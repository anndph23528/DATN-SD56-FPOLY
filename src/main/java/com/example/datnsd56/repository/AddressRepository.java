package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
    @Query(value = "SELECT * FROM Address WHERE province = ?1 or zipcode = ?1",
        nativeQuery = true)
    Page<Address> findAddressesByPhone(String phone, Pageable pageable);
    @Query(value = "SELECT * FROM address WHERE account_id = ?1", nativeQuery = true)
    Address findAccountDefaultAddress(@Param("accountId") Integer accountId);


    @Query(value = "SELECT DISTINCT * FROM address WHERE account_id = ?1", nativeQuery = true)
    List<Address> findAccountAddresses(@Param("accountId") Integer accountId);
    @Query(value = "SELECT * FROM address  WHERE account_id =?1 AND default_address = 1",nativeQuery = true)
    Address findDefaultAddress(@Param("accountId") Integer accountId);
}
