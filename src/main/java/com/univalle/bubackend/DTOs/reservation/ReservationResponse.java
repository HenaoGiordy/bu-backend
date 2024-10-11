package com.univalle.bubackend.DTOs.reservation;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationResponse(
        String message,
        Integer reservationId,
        LocalDateTime date,
        LocalTime time,
        Boolean paid,
        Boolean lunch,
        Boolean snack,
        String userName) {
}