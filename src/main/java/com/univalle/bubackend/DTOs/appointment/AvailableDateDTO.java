package com.univalle.bubackend.DTOs.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.univalle.bubackend.models.AvailableDates;

import java.time.LocalDateTime;

public record AvailableDateDTO(
        Integer id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dateTime,
        Integer professionalId,
        Boolean available,
        String typeAppointment
) {

}