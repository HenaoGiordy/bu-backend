package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.ReservationRequest;
import com.univalle.bubackend.DTOs.reservation.ReservationResponse;
import com.univalle.bubackend.services.reservation.IReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Integer id) {
        ReservationResponse response = reservationService.cancelReservation(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/register-payment")
    public ResponseEntity<ReservationPaymentResponse> registerPayment(@RequestBody ReservationPaymentRequest paymentRequest) {
        ReservationPaymentResponse response = reservationService.registerPayment(paymentRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<List<ReservationResponse>> findReservationByUsername(@PathVariable String username) {
        List<ReservationResponse> responses = Collections.singletonList(reservationService.findReservationByUsername(username));
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> responses = reservationService.getActiveReservations();
        return ResponseEntity.ok(responses);
    }
}
