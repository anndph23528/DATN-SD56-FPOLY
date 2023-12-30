//package com.example.datnsd56.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "orderTimeline")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class OrderTimeline {
//    @Id
//    @Column(name = "id")
//    private Integer id;
//
//    @Column(name = "status")
//    private String status;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "timestamp")
//    private LocalDate timestamp;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "account_id", referencedColumnName = "id")
//    private Account accountId;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
//    private Orders orderId;
//
//}
