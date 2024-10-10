package com.univalle.bubackend.services.reservation;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.ReservationRequest;
import com.univalle.bubackend.DTOs.reservation.ReservationResponse;

public interface IReservationService {
    ReservationResponse createReservation(ReservationRequest reservationRequest);
    ReservationResponse cancelReservation(Integer reservationId);
    ReservationPaymentResponse registerPayment(ReservationPaymentRequest paymentRequest);
}