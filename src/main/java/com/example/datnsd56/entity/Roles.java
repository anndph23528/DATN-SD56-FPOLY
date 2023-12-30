package com.example.datnsd56.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Table(name = "Roles")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
//    @NotBlank(message = "Không được để trống")
//    @Column(name = "code")
//    private String code;
    @NotBlank(message = "Không được để trống")
    @Column(name = "name")
    private String name;


    @Column(name = "status")
    private Boolean status;


}
