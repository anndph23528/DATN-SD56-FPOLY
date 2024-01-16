package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.entity.Size;
import com.example.datnsd56.repository.OrdersRepository;
import com.example.datnsd56.repository.ThongKeRepository;
import com.example.datnsd56.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThongKeServiceImpl implements ThongKeService {
    @Autowired
    private ThongKeRepository repository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Override
    public List<OrderItem> getAll() {
        return repository.getAllQ();
    }

    @Override
    public List<OrderItem> getAllhuy() {
        return repository.getAllByStatushuy();
    }

    @Override
    public Page<Orders> getAllhuy1() {
        Pageable pageable=PageRequest.of(0,10);
        return ordersRepository.findByOrderStatus(0,pageable);
    }
    @Override
    public Page<Orders> getAllhuy2() {
        Pageable pageable=PageRequest.of(0,10);
        return ordersRepository.findByOrderStatus(10,pageable);
    }
    @Override
    public Page<Orders> getAllhuy3() {
        Pageable pageable=PageRequest.of(0,10);
        return ordersRepository.findByOrderStatus(1,pageable);
    }
    @Override
    public Page<Orders> getAllhuy4() {
        Pageable pageable=PageRequest.of(0,10);
        return ordersRepository.findByOrderStatus(2,pageable);
    }

    @Override
    public Page<Orders> getAllhuy5() {
        Pageable pageable=PageRequest.of(0,10);
        return ordersRepository.findByOrderStatus(3,pageable);
    }

    @Override
    public List<OrderItem> getAllhoanthanh() {
        return repository.getAllByStatusdahoathanh();
    }

    @Override
    public List<OrderItem> getAlldanggiao() {
        return repository.getAllByStatusdanggiao();
    }

    @Override
    public List<OrderItem> getAllTop5() {
        return repository.getAllTop5();
    }

    @Override
    public List<OrderItem> getAllByTime(LocalDate tuNgay, LocalDate DenNgay) {
List<OrderItem> orderItemList=repository.getAllQ();
        if (tuNgay != null && DenNgay != null) {
            orderItemList = orderItemList
                .stream()
                .filter(history ->
                    !history.getOrders().getCreateDate().isBefore(tuNgay.atStartOfDay()) &&
                        !history.getOrders().getCreateDate().isAfter(DenNgay.atStartOfDay().plusDays(1)))
                .collect(Collectors.toList());
        }
        return orderItemList;
    }

    @Override
    public BigDecimal getToTal1() {
        return repository.getTotalProducts();
    }

    @Override
    public BigDecimal getToTalManey1() {
        return repository.getTotalManey();
    }

    @Override
    public BigDecimal getToTalHuy() {
        return repository.getTotalHuy();
    }

    @Override
    public BigDecimal getToTalHt() {
        return repository.getTotalHt();
    }

    @Override
    public BigDecimal getToTalAll() {
        return repository.getTotalAll();
    }

    @Override
    public BigDecimal getToTalAllManey() {
        return repository.getTotalAllManey();
    }

}
