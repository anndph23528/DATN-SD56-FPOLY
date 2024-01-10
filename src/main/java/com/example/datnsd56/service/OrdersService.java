
package com.example.datnsd56.service;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.entity.Cart;
import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersService {
    Orders detailHD(Integer id);

    List<Orders> getAll();

    List<Orders> getAllOrders();

    Page<Orders> getAllOrders(Integer page);

    Orders getOneBill(Integer id);

    void delete(Integer id);

    Orders update(Orders orders, Integer id);

    Orders planceOrder(Cart cart, String address);
    Orders applyVoucherToOrder(Orders order, Voucher voucher);
Optional<Orders> getOrderId(Integer id);

     BigDecimal getNewTotalAfterApplyingVoucher(String username);
    BigDecimal calculateDiscountValue(Voucher voucher, BigDecimal total);

    List<Orders> getNoConfirmOrders(Integer accountId);

    List<OrderItem> getLstDetailByOrderId(Integer id);

    List<Orders> getAllOrders1(Integer accountId);

    Orders cancelOrder(Integer Id,Account account);

    Orders acceptBill(Integer Id);

    Orders add(Orders hoaDon);

    Orders shippingOrder(Integer id, BigDecimal shippingFee);

    Orders completeOrder(Integer id,Account account);

//    Page<Orders> search(LocalDateTime createDate, LocalDateTime updateDate);
    Page<Orders> filterAndSearch(LocalDate startDate, LocalDate endDate, String searchInput, Pageable pageable);

    Page<Orders> findByPhone(String phone);

//    Orders create(JsonNode orderDate);
}
