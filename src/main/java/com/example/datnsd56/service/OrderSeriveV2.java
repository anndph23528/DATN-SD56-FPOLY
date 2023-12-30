package com.example.datnsd56.service;

import com.example.datnsd56.entity.Cart;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.entity.Voucher;
import com.example.datnsd56.entity.VoucherUsage;

import java.math.BigDecimal;
import java.util.List;

public interface OrderSeriveV2 {
    void applyVouchers(Orders order, String voucherCode);
    void processOrderDetails(Cart cart, Orders order);
    void reduceProductStock(Integer id, int quantity);
    Orders createOrder(Cart cart, String address);
//    void saveVoucherUsageHistoryOnOrder(Orders order, Voucher voucher);
    void applyVoucherWithoutSaving(Orders order, String voucherCode, String selectedVoucherCode);
    void applyVoucher(Orders order, String voucherCode, String selectedVoucherCode);
    Orders placeOrders(Cart cart, String address, String voucherCode, String selectedVoucherCode);
    Orders placeOrder(Cart cart, String address, String voucherCode, String selectedVoucherCode);
    BigDecimal calculateDiscountValue(Voucher voucher, BigDecimal total);
    BigDecimal calculateTotalPriceWithVoucher(String voucherCode, BigDecimal originalTotalPrice);
    void cancelVoucher(Cart cart, String voucherCode);
    BigDecimal calculateTotalWithVoucher(Cart cart, String selectedVoucherCode, String username);
    List<VoucherUsage> findByIsVisibleTrue();

}
