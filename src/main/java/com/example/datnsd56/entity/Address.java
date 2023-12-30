package com.example.datnsd56.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Table(name = "Address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
@NotBlank(message = "không được để trống !")
    @Column(name = "streetName")
    private String streetName;
    @NotBlank(message = "không được để trống !")

    @Column(name = "district")
    private String district;


    @NotBlank(message = "không được để trống !")

    @Column(name = "province")
    private String province;
    @NotBlank(message = "không được để trống !")

    @Column(name = "zipcode")
    private String zipcode;
@Column(name = "default_address")
private Boolean defaultAddress;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

}
