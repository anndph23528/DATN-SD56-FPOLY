package com.example.datnsd56.entity;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Voucher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//import javax.persistence.*;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "VoucherUsage")
public class VoucherUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    private Voucher voucher;

    @Column(name = "used_date")
    private LocalDateTime usedDate;

    @Column(name = "is_used")
    private Boolean isUsed;
    @Column(name = "isVisible")
    private Boolean isVisible;

    // Constructors, getters, setters...
}
