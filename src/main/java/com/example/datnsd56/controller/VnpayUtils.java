//package com.example.datnsd56.controller;
//
//import com.example.datnsd56.config.Config;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class VnpayUtils {
//
//
//
//    public static boolean isValidIPN(Map<String, String[]> paramMap) {
//        // Lấy chữ ký từ callback
//        String vnp_SecureHash = paramMap.get("vnp_SecureHash")[0];
//
//        // In ra dữ liệu callback và chữ ký
//        System.out.println("Callback data: " + paramMap);
//        System.out.println("vnp_SecureHash from VNPAY: " + vnp_SecureHash);
//
//        // Tạo một bản sao của paramMap để không làm ảnh hưởng đến original paramMap
//        Map<String, String[]> paramMapCopy = new HashMap<>(paramMap);
//
//        // Tạo chuỗi dữ liệu cần hash và thêm secret key
//        String calculatedHash = hashAllFields(paramMapCopy);
//
//        // In ra giá trị của chữ ký
//        System.out.println("calculatedHash: " + calculatedHash);
//
//        // So sánh chữ ký
//        return vnp_SecureHash.equals(calculatedHash);
//    }
//
//
//    public static String hashAllFields(Map<String, String[]> fields) {
//        // Tạo chuỗi dữ liệu cần hash
//        StringBuilder hashData = new StringBuilder();
//
//        // Sắp xếp các tham số theo key
//        String[] fieldNames = fields.keySet().toArray(new String[0]);
//        Arrays.sort(fieldNames);
//
//        for (String fieldName : fieldNames) {
//            String fieldValue = fields.get(fieldName)[0];
//            hashData.append(fieldName).append("=").append(fieldValue).append("&");
//        }
//
//        // Bỏ dấu & ở cuối chuỗi
//        hashData.setLength(hashData.length() - 1);
//
//        // Thêm secret key sau mỗi trường dữ liệu
//        hashData.append("&secretKey=").append(Config.secretKey);
//
//        // Hash chuỗi dữ liệu
//        return hmacSHA512(hashData.toString());
//    }
//
//
//    private static String hmacSHA512(final String data) {
//        try {
//            if (data == null) {
//                throw new NullPointerException();
//            }
//            final Mac hmac512 = Mac.getInstance("HmacSHA512");
//            byte[] hmacKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
//            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
//            hmac512.init(secretKey);
//            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
//            byte[] result = hmac512.doFinal(dataBytes);
//            StringBuilder sb = new StringBuilder(2 * result.length);
//            for (byte b : result) {
//                sb.append(String.format("%02x", b & 0xff));
//            }
//            return sb.toString();
//        } catch (Exception ex) {
//            return "";
//        }
//    }
//
//    // Thêm phương thức để set secretKey
//    public static void setSecretKey(String secretKey) {
//        VnpayUtils.secretKey = secretKey;
//    }
//}
