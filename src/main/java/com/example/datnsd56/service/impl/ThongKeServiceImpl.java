package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.entity.Size;
import com.example.datnsd56.repository.ThongKeRepository;
import com.example.datnsd56.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThongKeServiceImpl implements ThongKeService {
    @Autowired
    private ThongKeRepository repository;

    @Override
    public List<OrderItem> getAll() {
        return repository.getAllQ();
    }

    @Override
    public List<OrderItem> getAllhuy() {
        return repository.getAllByStatushuy();
    }

    @Override
    public List<OrderItem> getAllhoanthanh() {
        return repository.getAllByStatusdahoathanh();
    }

    @Override
    public List<OrderItem> getAlldanggiao() {
        return repository.getAllByStatusdanggiao();
    }
}
