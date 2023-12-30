package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    @Query(value = "SELECT * FROM Account WHERE  email = ?1",
        nativeQuery = true)
    List<Account> findByEmail1(String email);
//    @Query("SELECT a FROM Account a WHERE like :a.phone")
//    List<Account> findAccountByPhone(String phone);
@Query(value = "SELECT * FROM Account WHERE phone = ?1 or email = ?2",
    nativeQuery = true)

Page<Account> findAccountByPhone(String phone, Pageable pageable);

//    @Query("SELECT DISTINCT a FROM Account a LEFT JOIN FETCH a.role_id WHERE a.name = :username")
    Optional<Account> findById(Integer id);
@Query(value="select*from account where fullname = ?1",nativeQuery = true)
    Optional<Account> findByName(String username);
Account findByEmail (String email);
}

