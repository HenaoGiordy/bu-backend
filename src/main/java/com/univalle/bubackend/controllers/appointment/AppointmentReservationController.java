package com.univalle.bubackend.controllers.appointment;

import com.univalle.bubackend.DTOs.appointment.*;
import com.univalle.bubackend.DTOs.user.UserResponse;
import com.univalle.bubackend.services.appointment.reservation.IAppointmentReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/appointment-reservation")
@PreAuthorize("hasAnyRole('ESTUDIANTE', 'PSICOLOGO', 'ENFERMERO', 'ODONTOLOGO', 'FUNCIONARIO')")
public class AppointmentReservationController {

    private IAppointmentReservationService appointmentReservationService;

    @Operation(
            summary = "Reservar una cita",
            description = "Permite reservar una cita, dado un horario disponible"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cita reservada exitosamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseAppointmentReservation.class))}),
            @ApiResponse(responseCode = "400", description = "Error de validación en la solicitud",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado o token inválido",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado para realizar esta operación",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Debes Ingresar el Id del paciente y el Id del horario disponible de un profesional.",
            required = true,
            content = @Content(schema = @Schema(implementation = RequestAppointmentReservation.class))
    )
    @PostMapping
    public ResponseEntity<ResponseAppointmentReservation> reservation(@Valid @RequestBody RequestAppointmentReservation requestAppointmentReservation) {
        return new ResponseEntity<>(appointmentReservationService.reserveAppointment(requestAppointmentReservation), HttpStatus.CREATED);
    }



    @Operation(
            summary = "Obtener la citas del profesional",
            description = "Permite obtener todas la citas de un profesional con su Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todas las citas",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseAppointmentReservationProfessional.class))}),
            @ApiResponse(responseCode = "400", description = "Error de validación en la solicitud",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado o token inválido",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado para realizar esta operación",
                    content = @Content)

    })
    @Parameter(
            name = "id",
            description = "Debes ingresar el id del profesional del cual quieres obtener la citas ",
            required = true,
            example = "1"
    )
    @GetMapping("/professional/{id}")
    public ResponseEntity<ResponseAppointmentReservationProfessional> getAppointmentById( @PathVariable Integer id) {
        return new ResponseEntity<>(appointmentReservationService.allAppointmentProfessional(id), HttpStatus.OK);
    }

    @GetMapping("/professional/pending/{id}")
    public ResponseEntity<ResponseAppointmentReservationProfessional> getAppointmentByIdPending( @PathVariable Integer id) {
        return new ResponseEntity<>(appointmentReservationService.allAppointmentProfessionalPending(id), HttpStatus.OK);
    }

    @GetMapping("/professional/attended/{id}")
    public ResponseEntity<ResponseAppointmentReservationProfessional> getAppointmentAttended( @PathVariable Integer id) {
        return new ResponseEntity<>(appointmentReservationService.allAppointmentProfessionalAttended(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener la citas del estudiante",
            description = "Permite obtener todas la citas de un estudiante con su Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todas las citas",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseAppointmentReservationStudent.class))}),
            @ApiResponse(responseCode = "400", description = "Error de validación en la solicitud",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado o token inválido",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado para realizar esta operación",
                    content = @Content)
    })
    @Parameter(
            name = "id",
            description = "Debes ingresar el Id del estudiante del cual quieres obtener las citas",
            required = true,
            example = "1"
    )
    @GetMapping("/student/{id}")
    public ResponseEntity<ResponseAppointmentReservationStudent> getAppointmentByAppointmentId(@PathVariable Integer id) {
        return new ResponseEntity<>(appointmentReservationService.allAppointmentEstudiante(id), HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ResponseAppointmentCancel> cancelAppointment(@PathVariable Integer id) {
        return new ResponseEntity<>(appointmentReservationService.cancelReservation(id), HttpStatus.OK);
    }

    @PostMapping("/asistencia")
    public ResponseEntity<ResponseAssistanceAppointment> assitance(@RequestBody RequestAssistance requestAssistance){
        return new ResponseEntity<>(appointmentReservationService.assistance(requestAssistance), HttpStatus.ACCEPTED);
    }

    @PostMapping("/follow-up")
    public ResponseEntity<ResponseAppointmentFollowUp> followUp(@RequestBody RequestAppointmentFollowUp responseAppointmentFollowUp){
        return new ResponseEntity<>(appointmentReservationService.followUp(responseAppointmentFollowUp), HttpStatus.CREATED);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserResponseAppointment> findReservationByUsername(
            @PathVariable String username,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        UserResponseAppointment responses = appointmentReservationService.findReservationsByUsername(username, pageable);
        return ResponseEntity.ok(responses);
    }
}
