package com.univalle.bubackend.controllers.appointment;

import com.univalle.bubackend.DTOs.appointment.RequestAppointmentReservation;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservation;
import com.univalle.bubackend.DTOs.appointment.ResponseAppointmentReservationProfessional;
import com.univalle.bubackend.services.appointment.reservation.IAppointmentReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/professional/{id}")
    public ResponseEntity<ResponseAppointmentReservationProfessional> getAppointmentById(@PathVariable Integer id) {
        return new ResponseEntity<>(appointmentReservationService.allAppointmentProfessional(id), HttpStatus.OK);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<ResponseAppointmentReservationProfessional> getAppointmentByAppointmentId(@PathVariable Integer id) {
        return new ResponseEntity<>(appointmentReservationService.allAppointmentEstudiante(id), HttpStatus.OK);
    }
}
