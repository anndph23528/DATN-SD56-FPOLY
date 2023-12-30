package com.example.datnsd56.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "VoucherUsageHistory")
public class VoucherUsageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    private Voucher voucher;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
//    private Orders order;

    @Column(name = "used_date")
    private LocalDateTime usedDate;

    // Các trường khác mà bạn cần thêm, chẳng hạn như giảm giá thực tế, tổng giá trị đơn hàng, v.v.

    // Constructors, getters, setters...
}
