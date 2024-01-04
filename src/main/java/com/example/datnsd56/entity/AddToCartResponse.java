package com.example.datnsd56.entity;

public class AddToCartResponse {
    private String message;
    private int remainingQuantity;

    // Constructors, getters, setters...

    public AddToCartResponse(String message, int remainingQuantity) {
        this.message = message;
        this.remainingQuantity = remainingQuantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }
}
