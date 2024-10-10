package com.univalle.bubackend.DTOs.appointment;

import com.univalle.bubackend.DTOs.user.UserEntityDTO;
import com.univalle.bubackend.models.AppointmentReservation;

public record AppointmentReservationProfessionalDTO(AvailableDateDTO availableDate, UserEntityDTO professional) {

    public AppointmentReservationProfessionalDTO(AppointmentReservation appointmentReservation) {
        this(
                new AvailableDateDTO(appointmentReservation.getAvailableDates()),
                new UserEntityDTO(appointmentReservation.getEstudiante())
        );
    }

}
