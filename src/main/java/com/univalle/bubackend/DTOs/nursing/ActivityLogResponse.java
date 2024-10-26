package com.univalle.bubackend.DTOs.nursing;

import java.time.LocalDate;
import com.univalle.bubackend.models.Gender;
import com.univalle.bubackend.models.Diagnostic;
import jakarta.validation.constraints.Positive;

public record ActivityLogResponse(
        Integer id,
        LocalDate date,
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
