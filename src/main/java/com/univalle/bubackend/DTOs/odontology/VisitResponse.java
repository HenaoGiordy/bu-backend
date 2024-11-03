package com.univalle.bubackend.DTOs.odontology;


import com.univalle.bubackend.models.OdontologyReason;
import java.time.LocalDateTime;

public record VisitResponse(
        LocalDateTime date,
        OdontologyReason reason,
        String description
        ) {
}
