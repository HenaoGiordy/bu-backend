package com.univalle.bubackend.services.appointment.reservation;

import com.univalle.bubackend.DTOs.appointment.*;

public interface IAppointmentReservationService {

    ResponseAppointmentReservation reserveAppointment(RequestAppointmentReservation requestAppointmentReservation);

    ResponseAppointmentReservationProfessional allAppointmentProfessional(Integer professionalId);
    ResponseAppointmentReservationStudent allAppointmentEstudiante(Integer estudianteId);

    ResponseAppointmentCancel cancelReservation(Integer id);
}
