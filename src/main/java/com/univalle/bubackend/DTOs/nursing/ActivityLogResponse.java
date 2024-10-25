package com.univalle.bubackend.DTOs.nursing;

import java.time.LocalDate;
import com.univalle.bubackend.models.Gender;
import com.univalle.bubackend.models.Diagnostic;

public record ActivityLogResponse(
        Long id,
        LocalDate date,
        String username,
        String name,
        @jakarta.validation.constraints.Positive Long phone,
        String plan,
        String semester,
        Gender gender,
        Diagnostic diagnostic,
        String conduct
) {
}
