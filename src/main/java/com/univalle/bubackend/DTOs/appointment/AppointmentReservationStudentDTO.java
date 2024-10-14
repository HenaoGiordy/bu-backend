package com.univalle.bubackend.DTOs.appointment;

import com.univalle.bubackend.DTOs.user.UserEntityDTO;
import com.univalle.bubackend.models.AppointmentReservation;

public record AppointmentReservationStudentDTO(Integer reservationId, AvailableDateDTO availableDate, UserEntityDTO estudiante) {

    public AppointmentReservationStudentDTO(AppointmentReservation appointmentReservation) {
        this(
                appointmentReservation.getId(),
                new AvailableDateDTO(appointmentReservation.getAvailableDates()),
                new UserEntityDTO(appointmentReservation.getEstudiante())
        );
    }

}
