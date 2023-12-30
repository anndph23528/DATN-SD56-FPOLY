package com.example.datnsd56.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionInfo {


        private boolean isSuccess;
        private String transactionId;
        private String amount;
        private String orderId;
        private String bankCode;
        private String timeStamp;

        // Getter và Setter



    public void setSuccess(boolean equals) {
    }

    // Getters and setters

    // Add other fields as needed
}
