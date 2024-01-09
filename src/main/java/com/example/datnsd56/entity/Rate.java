package com.example.datnsd56.entity;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RateID")
    private Integer rateID;

    @Column(name = "rating")
    private Integer Rating;

    @Column(name = "Comment")
    private String comment;

    @Column(name = "Timestamp")
    private LocalDateTime timeStamp;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OrderDetailID")
    private OrderItem orderItem;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID")
    private Account account;
}
