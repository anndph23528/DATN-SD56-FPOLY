package com.example.datnsd56.service;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;

import java.util.List;

public interface OrderItemService {
    OrderItem detailHD(Integer id);

    void delete(Integer id);


    OrderItem add(OrderItem orderItem);

    void taoHoaDon(List<OrderItem> orderItems);
}
