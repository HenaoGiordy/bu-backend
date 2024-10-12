package com.univalle.bubackend.services.reservation;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.ListReservationResponse;
import com.univalle.bubackend.DTOs.reservation.ReservationRequest;
import com.univalle.bubackend.DTOs.reservation.ReservationResponse;
import com.univalle.bubackend.exceptions.reservation.NoSlotsAvailableException;
import com.univalle.bubackend.exceptions.reservation.ResourceNotFoundException;
import com.univalle.bubackend.exceptions.reservation.UnauthorizedException;
import com.univalle.bubackend.models.Reservation;
import com.univalle.bubackend.models.Setting;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.ReservationRepository;
import com.univalle.bubackend.repository.SettingRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final UserEntityRepository userEntityRepository;
    private final SettingRepository settingRepository;

    @Override
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        UserEntity user = userEntityRepository.findByUsername(reservationRequest.userName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Setting setting = settingRepository.findSettingById(1)
                .orElseThrow(() -> new ResourceNotFoundException("Configuración no encontrada"));


        LocalTime now = LocalTime.now();
        //Inicio reserva de almuerzo a beneficiarios
        if (reservationRequest.lunch() && user.getLunchBeneficiary() && now.isBefore(setting.getStarBeneficiaryLunch())) {
            throw new UnauthorizedException("No tienes acceso a reservar almuerzo. Todavía no inicia la venta");
        }
        //Inicio reserva de almuerzo a venta libre
        if (reservationRequest.lunch() && !user.getLunchBeneficiary() && now.isBefore(setting.getStarLunch())) {
            throw new UnauthorizedException("No tienes acceso a reservar almuerzo. Espera a venta libre");
        }
        //Finalización de reserva de almuerzos
        if (reservationRequest.lunch() && now.isAfter(setting.getEndLunch())) {
            throw new UnauthorizedException("No tienes acceso a reservar almuerzo. La venta ya finalizo");
        }

        //Inicio reserva de refrigerio a beneficiarios
        if (reservationRequest.snack() && user.getSnackBeneficiary() && now.isAfter(setting.getStarBeneficiarySnack())) {
            throw new UnauthorizedException("No tienes acceso a reservar refrigerio. Todavía no inicia la venta");
        }
        //Inicio reserva de refrigerio a venta libre
        if (reservationRequest.snack() && !user.getSnackBeneficiary() && now.isBefore(setting.getStarSnack())) {
            throw new UnauthorizedException("No tienes acceso a reservar refrigerio. Espera a venta libre");
        }
        //Finalización de reserva de refrigerio
        if (reservationRequest.snack() && now.isAfter(setting.getEndSnack())) {
            throw new UnauthorizedException("No tienes acceso a reservar refrigerio. La venta ya finalizo");
        }

        int remainingSlotsLunch = reservationRepository.countRemainingLunchSlotsForDay(LocalDate.now(), reservationRequest.lunch());
        int remainingSlotsSnack = reservationRepository.countRemainingSnacklotsForDay(LocalDate.now(), reservationRequest.snack());

        if (remainingSlotsLunch <= 0 && now.isBefore(setting.getEndLunch()) && now.isAfter(setting.getStarBeneficiaryLunch())) {
            throw new NoSlotsAvailableException("No quedan reservas de almuerzo disponibles para hoy.");
        }

        if (remainingSlotsSnack <= 0 && now.isBefore(setting.getEndSnack()) && now.isAfter(setting.getStarBeneficiarySnack())) {
            throw new NoSlotsAvailableException("No quedan reservas de almuerzo disponibles para hoy.");
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

        return new ReservationResponse(
                "Reserva realizada con éxito.",
                reservation.getId(),
                reservation.getData(),
                reservation.getTime(),
                reservation.getPaid(),
                reservation.getLunch(),
                reservation.getSnack(),
                reservation.getUserEntity().getUsername()
        );
    }

    @Override
    public List<ReservationResponse> getReservationsPerDay(String username){
        LocalDate date = LocalDate.now();

        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Reservation> reservations = reservationRepository.findReservationsPerDay(user, date);

        return reservations.stream()
                .map(reservation -> new ReservationResponse(
                        "Reserva activa encontrada.",
                        reservation.getId(),
                        reservation.getData(),
                        reservation.getTime(),
                        reservation.getPaid(),
                        reservation.getLunch(),
                        reservation.getSnack(),
                        reservation.getUserEntity().getUsername()
                )).collect(Collectors.toList());
    }

    @Override
    public ReservationResponse cancelReservation(Integer reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        Integer id = reservation.getId();
        LocalDateTime date = reservation.getData();
        LocalTime time = reservation.getTime();
        Boolean paid = reservation.getPaid();
        Boolean lunch = reservation.getLunch();
        Boolean snack = reservation.getSnack();
        String userName = reservation.getUserEntity().getUsername();

        reservationRepository.delete(reservation);

        return new ReservationResponse(
                "Reserva cancelada con éxito.",
                id,
                date,
                time,
                paid,
                lunch,
                snack,
                userName
        );
    }

    //buscar la reserva con el codigo del usuario
    @Override
    public ReservationResponse findReservationByUsername(String username) {

        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Reservation> reservations = reservationRepository.findByUserEntityAndPaidFalse(user);

        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron reservas pendientes para este usuario.");
        }

        Reservation latestReservation = reservations.stream()
                .max(Comparator.comparing(Reservation::getData))
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva más reciente."));

        return new ReservationResponse(
                "Reserva encontrada.",
                latestReservation.getId(),
                latestReservation.getData(),
                latestReservation.getTime(),
                latestReservation.getPaid(),
                latestReservation.getLunch(),
                latestReservation.getSnack(),
                user.getUsername()
        );
    }

    //registrar pago
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

        return new ReservationPaymentResponse("Pago registrado con éxito.", lastReservation.getId());
    }

    //tabla
    public Page<ListReservationResponse> getActiveReservations(Pageable pageable) {
        return reservationRepository.findAllByPaidFalse(pageable)
                .map(reservation -> new ListReservationResponse(
                        reservation.getId(),
                        reservation.getData(),
                        reservation.getTime(),
                        reservation.getPaid(),
                        reservation.getSnack(),
                        reservation.getLunch(),
                        reservation.getUserEntity().getUsername()
                ));
    }

}
