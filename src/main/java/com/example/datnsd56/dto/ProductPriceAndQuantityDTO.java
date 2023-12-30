package com.example.datnsd56.dto;

import java.math.BigDecimal;

public class ProductPriceAndQuantityDTO {
    private BigDecimal sellPrice;
    private Integer quantity;

    public ProductPriceAndQuantityDTO(BigDecimal sellPrice, Integer quantity) {
        this.sellPrice = sellPrice;
        this.quantity = quantity;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
