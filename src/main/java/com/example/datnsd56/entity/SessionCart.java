package com.example.datnsd56.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionCart {
    private BigDecimal totalPrice;

    private Integer totalItems;

    private LocalDate createDate;

    private LocalDate updateDate;

    private String status;

    private Set<SessionCartItem> cartItems;
}
