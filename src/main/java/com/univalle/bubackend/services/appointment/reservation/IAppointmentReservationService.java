package com.univalle.bubackend.services.appointment.reservation;

import com.univalle.bubackend.DTOs.appointment.RequestAppointmentReservation;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservation;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservationProfessional;

public interface IAppointmentReservationService {

    ResponseAppointmentReservation reserveAppointment(RequestAppointmentReservation requestAppointmentReservation);

    ResponseAppointmentReservationProfessional allAppointmentProfessional(Integer professionalId);
    ResponseAppointmentReservationProfessional allAppointmentEstudiante(Integer estudianteId);
}
