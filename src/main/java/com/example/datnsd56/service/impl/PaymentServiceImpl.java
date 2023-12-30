package com.example.datnsd56.service.impl;

import com.example.datnsd56.config.Config;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.entity.Transactions;
import com.example.datnsd56.service.OrdersService;
import com.example.datnsd56.service.TransactionService;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceImpl {
@Autowired
private OrdersService ordersService;
@Autowired
    TransactionService transactionService;

    // Inject các service khác nếu cần thiết

    public String createVnpPaymentUrl(BigDecimal amount, Integer orderId, Integer transactionId) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String bankCode = "NCB";
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = Config.vnp_TmnCode;
        long amountInLong = amount.multiply(new BigDecimal("100")).longValue();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf( amountInLong));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", getCurrentDate());
        vnp_Params.put("vnp_ExpireDate", getExpireDate());

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=')
                    .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return Config.vnp_PayUrl + "?" + queryUrl;
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        return formatter.format(calendar.getTime());
    }

    private String getExpireDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        calendar.add(Calendar.MINUTE, 15);
        return formatter.format(calendar.getTime());
    }
//    public void updatePaymentStatus(Integer orderId, String status) {
//        // Kiểm tra xem đơn hàng có tồn tại hay không
//        Optional<Orders> orderOptional = ordersService.getOrderId(orderId);
//        if (orderOptional.isPresent()) {
//            Orders cart = orderOptional.get();
//
//            // Kiểm tra xem trạng thái thanh toán có thay đổi hay không
//            if (!cart.getOrderStatus().equalsIgnoreCase(status)) {
//                // Cập nhật trạng thái thanh toán trong đơn hàng
//                cart.setOrderStatus("thanh con");
//                ordersService.add(cart);
//
//                // Cập nhật trạng thái thanh toán trong bảng Transactions
//               Transactions transaction = transactionService.getById(orderId);
//                if (transaction != null) {
//                    transaction.setStatus("thanh cong");
//                    transactionService.saveTransaction(transaction);
//                }
//
//                // Thực hiện các công việc khác nếu cần
//
//                // Log thông báo hoặc ghi nhật ký nếu cần thiết
//                System.out.println("Đã cập nhật trạng thái thanh toán cho đơn hàng #" + orderId + ": " + status);
//            } else {
//                System.out.println("Trạng thái thanh toán đã được cập nhật trước đó cho đơn hàng #" + orderId);
//            }
//        } else {
//            System.out.println("Không tìm thấy đơn hàng #" + orderId);
//            // Xử lý thông báo hoặc ghi nhật ký nếu cần thiết
//        }
//    }
}
