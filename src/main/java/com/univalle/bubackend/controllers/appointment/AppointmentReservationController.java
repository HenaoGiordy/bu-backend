package com.univalle.bubackend.controllers.appointment;

import com.univalle.bubackend.DTOs.appointment.RequestAppointmentReservation;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservation;
import com.univalle.bubackend.services.appointment.reservation.IAppointmentReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/appointment-reservation")
@PreAuthorize("hasAnyRole('ESTUDIANTE', 'PSICOLOGO', 'ENFERMERO', 'ODONTOLOGO')")
public class AppointmentReservationController {

    private IAppointmentReservationService appointmentReservationService;

    @PostMapping
    public ResponseEntity<ResponseAppointmentReservation> reservation(@Valid @RequestBody RequestAppointmentReservation requestAppointmentReservation) {
        return new ResponseEntity<>(appointmentReservationService.reserveAppointment(requestAppointmentReservation), HttpStatus.CREATED);
    }
}
