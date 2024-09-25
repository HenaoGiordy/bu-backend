package com.univalle.bubackend.DTOs.auth;

import jakarta.validation.constraints.NotBlank;

public record CreateMenuRequest(Integer id,
                                @NotBlank(message = "Ingrese el plato principal") String mainDish,
                                @NotBlank(message = "Ingrese la bebida") String drink,
                                String dessert,
                                @NotBlank(message = "Ingrese el precio") Integer price) {
}
