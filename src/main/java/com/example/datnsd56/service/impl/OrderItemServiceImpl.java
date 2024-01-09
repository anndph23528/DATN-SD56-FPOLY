package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Rate;
import com.example.datnsd56.repository.AccountRepository;
import com.example.datnsd56.repository.OrderItemRepository;
import com.example.datnsd56.repository.RateRepository;
import com.example.datnsd56.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public OrderItem detailHD(Integer id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    public void rateProduct(Integer orderItemId, Integer accountId, int rating, String comment) {
        // Retrieve the OrderItem entity from the database
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with ID: " + orderItemId));

        // Perform the rating and save it to the entity
        Rate rate = new Rate();
        rate.setRating(rating);
        rate.setComment(comment);
        rate.setTimeStamp(LocalDateTime.now());

        // Set relationships with OrderItem and Account entities
        rate.setOrderItem(orderItem);
        rate.setAccount(accountRepository.findById(accountId)
            .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId)));

        // Save the rate to the database
//        orderItem.addRate(rate);
        orderItemRepository.save(orderItem);
    }
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return orderItemRepository.findByOrdersId(orderId);
    }
    public void submitRating(Integer orderItemId, Integer accountId, Integer rating, String comment) throws AccessDeniedException {
        // Lấy hóa đơn chi tiết dựa trên orderItemId
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn chi tiết với ID: " + orderItemId));

        // Kiểm tra xem hóa đơn chi tiết có thuộc về người dùng đúng không
        if (!orderItem.getOrders().getAccountId().getId().equals(accountId)) {
            throw new AccessDeniedException("Bạn không có quyền đánh giá hóa đơn chi tiết này.");
        }

        // Tạo đối tượng đánh giá
        Rate rate = new Rate();
        rate.setRating(rating);
        rate.setComment(comment);

        // Liên kết đánh giá với hóa đơn chi tiết
        rate.setOrderItem(orderItem);

        // Lưu đánh giá vào cơ sở dữ liệu
        rateRepository.save(rate);
    }
    @Override
    public void delete(Integer id) {
    orderItemRepository.deleteById(id);
    }

    @Override
    public OrderItem add(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void taoHoaDon(List<OrderItem> orderItems) {
        orderItemRepository.saveAll(orderItems);
    }
}
