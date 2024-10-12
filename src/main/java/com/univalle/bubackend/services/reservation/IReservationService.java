package com.univalle.bubackend.services.reservation;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.ListReservationResponse;
import com.univalle.bubackend.DTOs.reservation.ReservationRequest;
import com.univalle.bubackend.DTOs.reservation.ReservationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReservationService {
    ReservationResponse createReservation(ReservationRequest reservationRequest);
    List<ReservationResponse> getReservationsPerDay(String username);
    ReservationResponse cancelReservation(Integer reservationId);
    ReservationResponse findReservationByUsername(String username);
    ReservationPaymentResponse registerPayment(ReservationPaymentRequest paymentRequest);
    Page<ListReservationResponse> getActiveReservations(Pageable pageable);
}