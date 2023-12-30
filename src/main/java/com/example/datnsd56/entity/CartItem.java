package com.example.datnsd56.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cart_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "status")
    private String status;
    @Column(name = "quantity")
    private Integer quantity ;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "create_date")
    private LocalDate createDate;
    @Column(name = "update_date")
    private LocalDate updateDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = " product_detail_id")
    private ProductDetails productDetails;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = " cart_id ", referencedColumnName = "Id")
    private Cart cart;
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
//    @JoinColumn(name = "cart_id", referencedColumnName = "id")
//    private Cart cart;

}
