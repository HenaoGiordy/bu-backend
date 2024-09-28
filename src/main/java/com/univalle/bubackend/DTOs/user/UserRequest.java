package com.univalle.bubackend.DTOs.user;

import com.univalle.bubackend.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record UserRequest(
        @NotBlank(message = "Debes proporcionar el usuario") String username,
        @NotBlank(message = "Debes proporcionar el nombre") String name,
        @NotBlank(message = "Debes proporcionar el apellido") String lastName,
        @NotBlank(message = "Debes proporcionar el email") @Email(message = "Formato de email incorrecto") String email,
        @NotBlank(message = "Debes proporcionar la contraseña") String password,
        @NotBlank(message = "Debes proporcionar el plan") String plan,
        @NotEmpty(message = "Debe tener por lo menos un rol")  Set<Role> roles
) {

}