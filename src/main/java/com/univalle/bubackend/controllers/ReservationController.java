package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.ListReservationResponse;
import com.univalle.bubackend.DTOs.reservation.ReservationRequest;
import com.univalle.bubackend.DTOs.reservation.ReservationResponse;
import com.univalle.bubackend.services.reservation.IReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @PostMapping("/create")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/per-day/{username}")
    public ResponseEntity<List<ReservationResponse>> getReservationsPerDay(@PathVariable String username) {
        List<ReservationResponse> responses = reservationService.getReservationsPerDay(username);
        return ResponseEntity.ok(responses);
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
    public ResponseEntity<Page<ListReservationResponse>> getAllReservations(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ListReservationResponse> reservations = reservationService.getActiveReservations(pageable);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
}
