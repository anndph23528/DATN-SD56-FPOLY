//package com.example.datnsd56.service.impl;
//
//import com.example.datnsd56.entity.OrderItem;
//import com.example.datnsd56.repository.OrderItemRepository;
//import com.example.datnsd56.service.OrderItemService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class OrderItemServiceImpl implements OrderItemService {
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//    @Override
//    public OrderItem detailHD(Integer id) {
//        return orderItemRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public void delete(Integer id) {
//    orderItemRepository.deleteById(id);
//    }
//
//    @Override
//    public OrderItem add(OrderItem orderItem) {
//        return orderItemRepository.save(orderItem);
//    }
//
//    @Override
//    public void taoHoaDon(List<OrderItem> orderItems) {
//        orderItemRepository.saveAll(orderItems);
//    }
//}
