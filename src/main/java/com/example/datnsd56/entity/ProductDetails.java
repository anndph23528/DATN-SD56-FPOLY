package com.example.datnsd56.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Product_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "quantity")
    @NotNull(message = "Không đuộc để trống!")
    private Integer quantity;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Products productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "size_id", referencedColumnName = "id")
    private Size sizeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color colorId;

}
