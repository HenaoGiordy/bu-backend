package com.univalle.bubackend.DTOs.reservation;

import jakarta.validation.constraints.NotBlank;

public record ReservationRequest(
        @NotBlank String userName,
        boolean lunch,
        boolean snack) {
}
