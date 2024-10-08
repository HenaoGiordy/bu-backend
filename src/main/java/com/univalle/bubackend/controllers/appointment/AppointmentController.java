package com.univalle.bubackend.controllers.appointment;

import com.univalle.bubackend.DTOs.appointment.RequestAvailableDate;
import com.univalle.bubackend.DTOs.appointment.ResponseAllAvailableDates;
import com.univalle.bubackend.DTOs.appointment.ResponseAvailableDate;
import com.univalle.bubackend.services.appointment.IAppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/appointment")
@SecurityRequirement(name = "Security Token")
@PreAuthorize("hasAnyRole('ODONTOLOGO', 'ENFERMERO', 'PSICOLOGO')")
public class AppointmentController {

    private IAppointmentService appointmentService;

    @PostMapping("/create-date")
    public ResponseEntity<ResponseAvailableDate> createDate(@Valid @RequestBody RequestAvailableDate requestAvailableDate) {
        return new ResponseEntity<>(appointmentService.availableDatesAssign(requestAvailableDate), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAllAvailableDates> getAllAvailableDatesProfessional(@PathVariable Integer id) {
        return new ResponseEntity<>(appointmentService.getAllDatesProfessional(id), HttpStatus.OK);
    }
}
