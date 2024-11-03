package com.univalle.bubackend.services.appointment.reservation;

import com.univalle.bubackend.DTOs.appointment.*;
import com.univalle.bubackend.DTOs.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAppointmentReservationService {

    ResponseAppointmentReservation reserveAppointment(RequestAppointmentReservation requestAppointmentReservation);

    ResponseAppointmentReservationProfessional allAppointmentProfessional(Integer professionalId);

    ResponseAppointmentReservationStudent allAppointmentEstudiante(Integer estudianteId);

    ResponseAppointmentCancel cancelReservation(Integer id);

    ResponseAssistanceAppointment assistance(RequestAssistance requestAssistance);

    ResponseAppointmentFollowUp followUp(RequestAppointmentFollowUp requestAppointmentFollowUp);

    UserResponse findReservationsByUsername(RequestUser requestUser);

    Page<ListReservationResponse> getReservations(Pageable pageable);
}