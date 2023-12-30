package com.example.datnsd56.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "totalPrice")
    private BigDecimal totalPrice;

    @Column(name = "totalItems")
    private Integer totalItems;



    @Column(name = "status")
    private String status;



    @Column(name = "create_date")
    private LocalDate createDate;


    @Column(name = "update_date")
    private LocalDate updateDate;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "cart")
    private Set<CartItem> cartItems;
//    @ManyToOne
//    @JoinColumn(name = "account_id", referencedColumnName = "id")
//    private Account accountId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account accountId;

    public Cart(){
        this.cartItems = new HashSet<>();
        this.totalItems = 0;
        this.totalPrice = BigDecimal.valueOf(0);
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
        this.status = "0";
    }



}
