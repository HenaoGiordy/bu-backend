package com.univalle.bubackend.DTOs.reservation;

import jakarta.validation.constraints.NotBlank;

public record ReservationExternRequest(
        @NotBlank String userName,
        @NotBlank String name,
        @NotBlank String lastname,
        @NotBlank String plan,
        @NotBlank String email,
        boolean lunch,
        boolean snack) {
}
