package com.univalle.bubackend.services.appointment.reservation;

import com.univalle.bubackend.DTOs.appointment.RequestAppointmentReservation;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservation;

public interface IAppointmentReservationService {

    ResponseAppointmentReservation reserveAppointment(RequestAppointmentReservation requestAppointmentReservation);
}
