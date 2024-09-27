package com.univalle.bubackend.DTOs.auth;

import lombok.Builder;

@Builder
public record CreateMenuRequest(Integer id, String mainDish, String drink, String dessert, Integer price) {
}
