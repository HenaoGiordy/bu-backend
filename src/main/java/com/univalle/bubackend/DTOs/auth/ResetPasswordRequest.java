package com.univalle.bubackend.DTOs.auth;

public record ResetPasswordRequest(String password, String passwordConfirmation) {
}
