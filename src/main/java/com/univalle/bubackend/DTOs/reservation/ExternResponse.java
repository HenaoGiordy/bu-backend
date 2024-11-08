package com.univalle.bubackend.DTOs.reservation;

public record ExternResponse(
        Integer id,
        String username,
        String name,
        String lastname,
        String plan,
        String email
) {
}
