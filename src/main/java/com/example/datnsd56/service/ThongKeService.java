package com.example.datnsd56.service;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.entity.Size;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ThongKeService {

    List<OrderItem> getAll();
    List<OrderItem> getAllhuy();
    List<OrderItem> getAllhoanthanh();
    List<OrderItem> getAlldanggiao();

}
