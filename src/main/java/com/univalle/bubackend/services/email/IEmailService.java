package com.univalle.bubackend.services.email;

import com.univalle.bubackend.models.Reservation;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface IEmailService {
    void sendPasswordResetEmail(String email, String token);

    void sendReservationCancellationEmail(String type, Reservation reservation, LocalDateTime date, LocalTime time);
}
