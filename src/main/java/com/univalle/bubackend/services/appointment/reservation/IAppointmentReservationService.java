package com.univalle.bubackend.services.appointment.reservation;

import com.univalle.bubackend.DTOs.appointment.RequestAppointmentReservation;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservation;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservationProfessional;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservationStudent;

public interface IAppointmentReservationService {

    ResponseAppointmentReservation reserveAppointment(RequestAppointmentReservation requestAppointmentReservation);

    ResponseAppointmentReservationProfessional allAppointmentProfessional(Integer professionalId);
    ResponseAppointmentReservationStudent allAppointmentEstudiante(Integer estudianteId);
}
