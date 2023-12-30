package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    @Query(value = "select * from cart where account_id =?1",nativeQuery = true)
    Cart findByAccountId(Integer id);

}
