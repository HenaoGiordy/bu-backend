package com.univalle.bubackend.services.reservation;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.AvailabilityResponse;
import com.univalle.bubackend.DTOs.reservation.ListReservationResponse;
import com.univalle.bubackend.DTOs.reservation.ReservationRequest;
import com.univalle.bubackend.DTOs.reservation.ReservationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IReservationService {
    ReservationResponse createReservation(ReservationRequest reservationRequest);
    AvailabilityResponse getAvailability();
    List<ReservationResponse> getReservationsPerDay(String username);
    ReservationResponse cancelReservation(Integer reservationId);
    ReservationPaymentResponse registerPayment(ReservationPaymentRequest paymentRequest);
    Page<ListReservationResponse> getActiveReservations(Pageable pageable);
}