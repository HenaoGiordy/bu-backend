package com.univalle.bubackend.services.email;

import com.univalle.bubackend.models.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {

    public JavaMailSender mailSender;

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText("Para restablecer tu contraseña, haz clic en el siguiente enlace: " +
                "http://localhost:5173/confirmarcontrasena?token=" + token);
        mailSender.send(message);
    }

    @Override
    public void sendReservationCancellationEmail(String type, Reservation reservation, LocalDateTime date, LocalTime time) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reservation.getUserEntity().getEmail());
        message.setSubject("Confirmación de Cancelación de Reserva");
        message.setText("Su reserva de " + type + " ha sido cancelada con éxito el día " + date.toLocalDate() +
                " a las " + time + ".");
        mailSender.send(message);
    }
}