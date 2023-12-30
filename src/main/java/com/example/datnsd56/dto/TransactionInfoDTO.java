package com.example.datnsd56.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInfoDTO {
    private boolean isSuccess;
    private String transactionId;
    private String amount;
    private String orderId;
    private String bankCode;
    private String timeStamp;

    public void setIsSuccess(boolean vnp_transactionStatus) {
    }

    // Constructors, getters, and setters
}
