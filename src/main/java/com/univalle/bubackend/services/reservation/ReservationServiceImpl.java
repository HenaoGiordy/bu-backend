package com.univalle.bubackend.services.reservation;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.ReservationRequest;
import com.univalle.bubackend.DTOs.reservation.ReservationResponse;
import com.univalle.bubackend.exceptions.reservation.NoSlotsAvailableException;
import com.univalle.bubackend.exceptions.reservation.ResourceNotFoundException;
import com.univalle.bubackend.exceptions.reservation.UnauthorizedException;
import com.univalle.bubackend.models.Reservation;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.ReservationRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        UserEntity user = userEntityRepository.findByUsername(reservationRequest.userName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        LocalTime now = LocalTime.now();
        if (reservationRequest.lunch() && !user.getLunchBeneficiary() && now.isBefore(LocalTime.of(10, 0))) {
            throw new UnauthorizedException("No tienes acceso a reservar almuerzo. Espera a venta libre");
        }
        if (reservationRequest.snack() && !user.getSnackBeneficiary() && now.isBefore(LocalTime.of(18, 0))) {
            throw new UnauthorizedException("No tienes acceso a reservar refrigerio. Espera a venta libre");
        }

        int remainingSlots = reservationRepository.countRemainingSlotsForDay(LocalDate.now(), reservationRequest.lunch());
        if (remainingSlots <= 0) {
            throw new NoSlotsAvailableException("No quedan reservas disponibles para hoy.");
        }

        Reservation reservation = Reservation.builder()
                .userEntity(user)
                .lunch(reservationRequest.lunch())
                .snack(reservationRequest.snack())
                .data(LocalDateTime.now())
                .time(LocalTime.now())
                .paid(false)
                .build();

        reservationRepository.save(reservation);

        return new ReservationResponse("Reserva realizada con éxito.", reservation.getId());
    }

    @Override
    public ReservationResponse cancelReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));


        reservationRepository.delete(reservation);

        return new ReservationResponse("Reserva cancelada con éxito.", Math.toIntExact(reservationId));
    }

    @Override
    public ReservationPaymentResponse registerPayment(ReservationPaymentRequest paymentRequest) {
        UserEntity user = userEntityRepository.findByUsername(paymentRequest.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Reservation> reservations = reservationRepository.findByUserEntityAndPaidFalse(user);

        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron reservas pendientes para este usuario.");
        }

        Reservation lastReservation = reservations.stream()
                .max(Comparator.comparing(Reservation::getData))
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva más reciente."));

        lastReservation.setPaid(paymentRequest.paid());
        reservationRepository.save(lastReservation);

        return new ReservationPaymentResponse("Pago registrado con éxito.", reservations.get(0).getId());
    }
}
