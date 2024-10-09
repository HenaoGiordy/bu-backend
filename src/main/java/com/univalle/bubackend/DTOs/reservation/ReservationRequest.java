package com.univalle.bubackend.DTOs.reservation;

public record ReservationRequest(
        String userName,
        boolean lunch,
        boolean snack) {
}
