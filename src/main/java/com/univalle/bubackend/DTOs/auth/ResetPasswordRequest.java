package com.univalle.bubackend.DTOs.auth;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "debes ingresar una contraseña") String password,
        @NotBlank(message = "debes confirmar la contraseña") String passwordConfirmation) {
}
