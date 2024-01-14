package com.example.datnsd56.service;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.entity.Size;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ThongKeService {

    List<OrderItem> getAll();
    List<OrderItem> getAllhuy();
    Page<Orders> getAllhuy1();
    Page<Orders> getAllhuy2();
    Page<Orders> getAllhuy3();
    Page<Orders> getAllhuy4();
    Page<Orders> getAllhuy5();
    List<OrderItem> getAllhoanthanh();
    List<OrderItem> getAlldanggiao();
    List<OrderItem> getAllTop5();
    List<OrderItem> getAllByTime(String tuNgay, String DenNgay);
    BigDecimal getToTal1();
    BigDecimal getToTalManey1();
    BigDecimal getToTalHuy();
    BigDecimal getToTalHt();
    BigDecimal getToTalAll();
    BigDecimal getToTalAllManey();

}
