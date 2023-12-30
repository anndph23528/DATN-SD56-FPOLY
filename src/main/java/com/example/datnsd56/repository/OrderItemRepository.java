package com.example.datnsd56.repository;

import com.example.datnsd56.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findAllByOrdersId(Integer id);
}
