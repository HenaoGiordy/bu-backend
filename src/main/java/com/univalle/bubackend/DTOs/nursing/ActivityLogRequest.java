package com.univalle.bubackend.DTOs.nursing;

import com.univalle.bubackend.models.Diagnostic;
import com.univalle.bubackend.models.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ActivityLogRequest(
        @NotNull LocalDateTime date,
        @NotBlank String username,
        @NotNull @Positive Integer phone,
        @NotBlank String semester,
        @NotNull Gender gender,
        @NotNull Diagnostic diagnostic,
        @NotBlank String conduct
) {
}
