package com.univalle.bubackend.DTOs.user;

import java.io.Serializable;

public record PasswordRequest(String username, String password, String newPassword, String confirmPassword) {
}
