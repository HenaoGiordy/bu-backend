package com.univalle.bubackend.DTOs.nursing;

import com.univalle.bubackend.models.Diagnostic;
import com.univalle.bubackend.models.NursingActivityLog;

import java.time.LocalDate;

public record ActivityNursingResponse(
        Integer id,
        LocalDate date,
        UserResponse user,
        Diagnostic diagnostic,
        String conduct
) {
    public ActivityNursingResponse(NursingActivityLog activity) {
        this(
                activity.getId(),
                activity.getDate(),
                new UserResponse(activity.getUser()),
                activity.getDiagnostic(),
                activity.getConduct()
        );
    }
}
