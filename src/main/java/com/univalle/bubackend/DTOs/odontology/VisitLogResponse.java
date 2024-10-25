package com.univalle.bubackend.DTOs.odontology;

import com.univalle.bubackend.models.OdontologyReason;

import java.time.LocalDate;

public record VisitLogResponse(
        Long id,
        LocalDate date,
        String username,
        String name,
        String plan,
        OdontologyReason reason,
        String description
) {
}
