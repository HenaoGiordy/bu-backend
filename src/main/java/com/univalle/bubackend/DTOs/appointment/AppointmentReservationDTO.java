package com.univalle.bubackend.DTOs.appointment;

import com.univalle.bubackend.DTOs.user.UserEntityDTO;
import com.univalle.bubackend.models.AppointmentReservation;

public record AppointmentReservationDTO(AvailableDateDTO availableDate, UserEntityDTO estudiante) {

    public AppointmentReservationDTO(AppointmentReservation appointmentReservation) {
        this(
                new AvailableDateDTO(appointmentReservation.getAvailableDates()),
                new UserEntityDTO(appointmentReservation.getEstudiante())
        );
    }

}
