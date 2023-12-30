package com.example.datnsd56.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionCartItem {
    private Integer quantity;

    private BigDecimal price;

    private LocalDate createDate;

    private LocalDate updateDate;

    private String status;

    private SessionCart cart;

    private ProductDetails productDetail;
}
