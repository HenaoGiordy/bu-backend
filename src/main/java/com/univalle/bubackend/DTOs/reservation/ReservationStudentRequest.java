package com.univalle.bubackend.DTOs.reservation;

import jakarta.validation.constraints.NotBlank;

public record ReservationStudentRequest(
        @NotBlank String userName,
        boolean lunch,
        boolean snack) {
}
