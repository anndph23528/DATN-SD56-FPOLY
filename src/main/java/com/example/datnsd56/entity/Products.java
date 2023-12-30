package com.example.datnsd56.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    @NotBlank(message = "Opps, không được để trống")
    private String code;

    @Column(name = "name")
    @NotBlank(message = "Opps, không được để trống")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shoe_sole_id", referencedColumnName = "id")
    private ShoeSole shoeSoleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category categoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    private Material materialId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brandId;
@OneToMany(mappedBy = "productId",cascade =CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Image> images;
@OneToMany(mappedBy = "productId",cascade = CascadeType.ALL)
    private List<ProductDetails> productDetails;
}
