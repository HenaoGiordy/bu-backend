package com.univalle.bubackend.services.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
}