package com.example.datnsd56.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rate")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Rate {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "score")
    private Integer score;

    @Column(name = "comment")
    private String comment;

    @Column(name = "createDate")
    private LocalDate createDate;

}
