package com.univalle.bubackend.DTOs.appointment;

import com.univalle.bubackend.DTOs.user.UserEntityDTO;
import com.univalle.bubackend.models.AppointmentReservation;

public record AppointmentReservationProfessionalDTO(Integer reservationId, AvailableDateDTO availableDate, UserEntityDTO professional) {

    public AppointmentReservationProfessionalDTO(AppointmentReservation appointmentReservation) {
        this(
                appointmentReservation.getId(),
                new AvailableDateDTO(appointmentReservation.getAvailableDates()),
                new UserEntityDTO(appointmentReservation.getAvailableDates().getProfessional())
        );
    }

}
