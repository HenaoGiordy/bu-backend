package com.univalle.bubackend.models;

import com.univalle.bubackend.DTOs.CreateMenuRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    private String dessert;

    @Column(nullable = false)
    private Integer price;

    private String note;

    public Menu(CreateMenuRequest createMenuRequest) {
        this.mainDish = createMenuRequest.mainDish();
        this.drink = createMenuRequest.drink();
        this.dessert = createMenuRequest.dessert();
        this.price = createMenuRequest.price();
        this.note = createMenuRequest.note();
    }
}