package com.univalle.bubackend.DTOs.nursing;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.Positive;
import com.univalle.bubackend.models.Gender;
import com.univalle.bubackend.models.Diagnostic;

public record ActivityLogResponse(
        Long id,
        LocalDate date,
        LocalTime time,
        String username,
        String name,
        @Positive Long phone,
        String plan,
        String semester,
        Gender gender,
        Diagnostic diagnostic,
        String conduct
) {
}
