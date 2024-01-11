package com.example.datnsd56.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.core.annotation.Order;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Không được để trống !")
    @Column(name = "Code")
    private String code;
    @NotBlank(message = "Không được để trống !")
    @Column(name = "Description")
    private String description;
    @Column(name = "StartDate")
    private LocalDateTime startDate;
    @Future(message = "chọn thời gian lớn hơn thời gian hiện tại")
    @Column(name = "ExpiryDate")
    private LocalDateTime expiryDateTime;
//    @Max(value = 100, message = "Giảm giá không được lớn hơn 100%")
    @Column(name = "Discount")
    private BigDecimal discount;

    @NotNull(message = "Không được để trống !")
    @Max(value = 100,message = "Số lượng phải nhỏ hơn 100")
    @Column(name = "quantity")
    private Integer quantity; // Số lượng voucher có sẵn
    @Column(name = "Active")
    private boolean active;
    @Column(name = "isVisible")
    private Boolean isVisible;
    @Column(name = "DiscountType")
    private DiscountType discountType;
    @NotNull(message = "Không được để trống !")
    @Min(value = 100000,message = "đơn tối thiểu phải từ 100.000 vnd trở  lên")
    @Column(name = "minOrderAmount")
    private BigDecimal minOrderAmount;
    @OneToMany(mappedBy = "voucher",cascade = CascadeType.ALL)
    private List<Orders> orders;
    // Constructors, getters, setters
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    private Set<VoucherUsage> voucherUsages;
}
