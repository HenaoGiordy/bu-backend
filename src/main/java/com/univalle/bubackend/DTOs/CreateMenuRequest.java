package com.univalle.bubackend.DTOs;

import lombok.Builder;

@Builder
public record CreateMenuRequest(Integer id, String mainDish, String drink, String dessert, Integer price, String note) {
}
