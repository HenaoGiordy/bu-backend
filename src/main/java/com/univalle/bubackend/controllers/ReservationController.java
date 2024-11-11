package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.*;
import com.univalle.bubackend.services.reservation.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;
    @Operation(summary = "Crear reserva de estudiante", description = "Crea una nueva reserva para un estudiante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<ReservationResponse> createStudentReservation(@Valid @RequestBody ReservationStudentRequest request) {
        try {
            ReservationResponse response = reservationService.createStudentReservation(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Crear reserva de externo", description = "Crea una nueva reserva para un usuario externo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content)
    })
    @PostMapping("/create-extern")
    public ResponseEntity<ReservationResponse> createExternReservation(@Valid @RequestBody ReservationExternRequest request) {
        try {
            ReservationResponse response = reservationService.createExternReservation(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @Operation(summary = "Buscar reserva de externo por username", description = "Obtiene la reserva de un externo por su username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExternResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
    })
    @GetMapping("/extern/{username}")
    public ResponseEntity<ExternResponse> findExternUsername(@PathVariable String username) {
        try {
            ExternResponse responses = reservationService.getExtern(username);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener reservas por día", description = "Obtiene las reservas de un usuario para un día específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @GetMapping("/per-day/{username}")
    public ResponseEntity<List<ReservationResponse>> getReservationsPerDay(@PathVariable String username) {
        try {
            List<ReservationResponse> responses = reservationService.getReservationsPerDay(username);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Cancelar reserva", description = "Cancela una reserva existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
    })
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Integer id) {
        try {
            ReservationResponse response = reservationService.cancelReservation(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @Operation(summary = "Buscar reservas por username", description = "Obtiene todas las reservas de un usuario por su username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @GetMapping("/by-username/{username}")
    public ResponseEntity<List<ReservationResponse>> findReservationByUsername(@PathVariable String username) {
        try {
            List<ReservationResponse> responses = Collections.singletonList(reservationService.findReservationByUsername(username));
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @Operation(summary = "Registrar pago de reserva", description = "Registra el pago de una reserva existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationPaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content)
    })
    @PutMapping("/register-payment")
    public ResponseEntity<ReservationPaymentResponse> registerPayment(@RequestBody ReservationPaymentRequest paymentRequest) {
        try {
            ReservationPaymentResponse response = reservationService.registerPayment(paymentRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Obtener todas las reservas", description = "Obtiene todas las reservas activas con paginación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListReservationResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/all")
    public ResponseEntity<Page<ListReservationResponse>> getAllReservations(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<ListReservationResponse> reservations = reservationService.getActiveReservations(pageable);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Obtener disponibilidad", description = "Obtiene la disponibilidad para reservas de almuerzo y refrigerio.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvailabilityResponse.class)))
    })
    @GetMapping("/availability")
    public ResponseEntity<AvailabilityResponse> getAvailability() {
        AvailabilityResponse responses = reservationService.getAvailability();
        return ResponseEntity.ok(responses);
    }


    @Operation(summary = "Obtener disponibilidad por hora", description = "Obtiene la disponibilidad de reservas por hora.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad por hora encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvailabilityPerHourResponse.class)))
    })
    @GetMapping("/availability-per-hour")
    public AvailabilityPerHourResponse getAvailabilityPerHour() {
        return reservationService.getAvailabilityPerHour();
    }

}
