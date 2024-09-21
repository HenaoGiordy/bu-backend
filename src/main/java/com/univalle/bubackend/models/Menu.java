package com.univalle.bubackend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String mainDish;

    @Column(nullable = false)
    private String drink;

    @Column(nullable = false)
    private String dessert;

    @Column(nullable = false)
    private Integer price;
}