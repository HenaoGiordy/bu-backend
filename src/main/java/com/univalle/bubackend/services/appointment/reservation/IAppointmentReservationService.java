package com.univalle.bubackend.services.appointment.reservation;

import com.univalle.bubackend.DTOs.appointment.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAppointmentReservationService {

    ResponseAppointmentReservation reserveAppointment(RequestAppointmentReservation requestAppointmentReservation);

    ResponseAppointmentReservationProfessional allAppointmentProfessional(Integer professionalId);

    ResponseAppointmentReservationProfessional allAppointmentProfessionalPending(Integer professionalId);

    ResponseAppointmentReservationStudent allAppointmentEstudiante(Integer estudianteId);

    ResponseAppointmentCancel cancelReservation(Integer id);

    ResponseAssistanceAppointment assistance(RequestAssistance requestAssistance);

    ResponseAppointmentFollowUp followUp(RequestAppointmentFollowUp requestAppointmentFollowUp);

    UserResponseAppointment findReservationsByUsername(String username, Pageable pageable);

    Page<ListReservationResponse> getReservations(Pageable pageable, String username);
}