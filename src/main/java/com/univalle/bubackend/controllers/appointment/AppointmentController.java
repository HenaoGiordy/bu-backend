package com.univalle.bubackend.controllers.appointment;

import com.univalle.bubackend.DTOs.appointment.RequestAvailableDate;
import com.univalle.bubackend.DTOs.appointment.ResponseAvailableDate;
import com.univalle.bubackend.services.appointment.IAppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/appointment")
@SecurityRequirement(name = "Security Token")
@PreAuthorize("hasAnyRole('ODONTOLOGO', 'ENFERMERO', 'PSICOLOGO')")
public class AppointmentController {

    private IAppointmentService appointmentService;

    @PostMapping("/create-date")
    public ResponseEntity<ResponseAvailableDate> createDate(@RequestBody RequestAvailableDate requestAvailableDate) {
        return new ResponseEntity<>(appointmentService.availableDatesAssign(requestAvailableDate), HttpStatus.CREATED);
    }
}
