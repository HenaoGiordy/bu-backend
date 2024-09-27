package com.univalle.bubackend.services.email;

public interface IEmailService {
    void sendPasswordResetEmail(String email, String token);
}
