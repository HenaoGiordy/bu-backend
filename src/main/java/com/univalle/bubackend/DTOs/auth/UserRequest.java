package com.univalle.bubackend.DTOs.auth;

import com.univalle.bubackend.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record UserRequest(
        @NotBlank String username,
        @NotBlank String name,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String plan,
        @NotEmpty Set<Role> roles
) {

}