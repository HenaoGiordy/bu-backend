package com.univalle.bubackend.services.reservation;

import com.univalle.bubackend.DTOs.payment.ReservationPaymentRequest;
import com.univalle.bubackend.DTOs.payment.ReservationPaymentResponse;
import com.univalle.bubackend.DTOs.reservation.*;
import com.univalle.bubackend.DTOs.user.UserRequest;
import com.univalle.bubackend.exceptions.reservation.NoSlotsAvailableException;
import com.univalle.bubackend.exceptions.ResourceNotFoundException;
import com.univalle.bubackend.exceptions.reservation.UnauthorizedException;
import com.univalle.bubackend.models.Reservation;
import com.univalle.bubackend.models.RoleName;
import com.univalle.bubackend.models.Setting;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.ReservationRepository;
import com.univalle.bubackend.repository.SettingRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import com.univalle.bubackend.services.user.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final UserEntityRepository userEntityRepository;
    private final SettingRepository settingRepository;
    private final UserServiceImpl userService;

    @Override
    public ReservationUserResponse createReservation(UserEntity user, boolean lunch, boolean snack) {

        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        Setting setting = settingRepository.findSettingById(1)
                .orElseThrow(() -> new ResourceNotFoundException("Configuración no encontrada"));


        List<Reservation> lunchReservation = reservationRepository.findLunchReservationByUser(user.getUsername(), today);
        List<Reservation> snackReservation = reservationRepository.findSnackReservationByUser(user.getUsername(), today);

        // Inicio reserva de almuerzo a beneficiarios
        if (lunch && user.getLunchBeneficiary() && now.isBefore(setting.getStarBeneficiaryLunch())) {
            throw new UnauthorizedException("No tienes acceso a reservar almuerzo. Todavía no inicia la venta.");
        }
        // Inicio reserva de almuerzo a venta libre
        if (lunch && !user.getLunchBeneficiary() && now.isBefore(setting.getStarLunch())) {
            throw new UnauthorizedException("No tienes acceso a reservar almuerzo. Espera a venta libre.");
        }
        // Finalización de reserva de almuerzos
        if (lunch && now.isAfter(setting.getEndLunch())) {
            throw new UnauthorizedException("No tienes acceso a reservar almuerzo. La venta ya finalizó.");
        }

        // Inicio reserva de refrigerio a beneficiarios
        if (snack && user.getSnackBeneficiary() && now.isBefore(setting.getStarBeneficiarySnack())) {
            throw new UnauthorizedException("No tienes acceso a reservar refrigerio. Todavía no inicia la venta.");
        }
        // Inicio reserva de refrigerio a venta libre
        if (snack && !user.getSnackBeneficiary() && now.isBefore(setting.getStarSnack())) {
            throw new UnauthorizedException("No tienes acceso a reservar refrigerio. Espera a venta libre.");
        }
        // Finalización de reserva de refrigerio
        if (snack && now.isAfter(setting.getEndSnack())) {
            throw new UnauthorizedException("No tienes acceso a reservar refrigerio. La venta ya finalizó.");
        }

        // Llamar a getAvailability para obtener los slots restantes
        AvailabilityResponse availability = getAvailability();

        // Validación de slots disponibles para almuerzo
        if (lunch && availability.remainingSlotsLunch() <= 0) {
            throw new NoSlotsAvailableException("No quedan reservas de almuerzo disponibles para hoy.");
        }

        // Validación de slots disponibles para refrigerio
        if (snack && availability.remainingSlotsSnack() <= 0) {
            throw new NoSlotsAvailableException("No quedan reservas de refrigerio disponibles para hoy.");
        }

        if (!lunchReservation.isEmpty() && now.isBefore(setting.getStarBeneficiaryLunch()) && now.isAfter(setting.getEndLunch())) {
            throw new UnauthorizedException("El usuario ya realizó una reserva el día de hoy");
        }
        if (!snackReservation.isEmpty() && now.isBefore(setting.getStarBeneficiarySnack()) && now.isAfter(setting.getEndSnack())) {
            throw new UnauthorizedException("El usuario ya realizó una reserva el día de hoy");
        }

        // Crear la reserva
        Reservation reservation = Reservation.builder()
                .userEntity(user)
                .lunch(lunch)
                .snack(snack)
                .data(LocalDateTime.now())
                .time(LocalTime.now())
                .paid(false)
                .build();

        reservationRepository.save(reservation);

        return new ReservationUserResponse(
                "Reserva realizada con éxito.",
                reservation.getId(),
                reservation.getData(),
                reservation.getTime(),
                reservation.getPaid(),
                reservation.getLunch(),
                reservation.getSnack(),
                reservation.getUserEntity().getUsername(),
                reservation.getUserEntity().getName(),
                reservation.getUserEntity().getLastName()
        );
    }

    @Override
    public ReservationResponse createStudentReservation(ReservationStudentRequest reservationRequest) {

        UserEntity user = userEntityRepository.findByUsernameWithRole(reservationRequest.userName(), RoleName.ESTUDIANTE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        ReservationUserResponse reservationUserResponse = createReservation(user, reservationRequest.lunch(), reservationRequest.snack());
        return new ReservationResponse(
                reservationUserResponse.message(),
                reservationUserResponse.reservationId(),
                reservationUserResponse.date(),
                reservationUserResponse.time(),
                reservationUserResponse.paid(),
                reservationUserResponse.lunch(),
                reservationUserResponse.snack(),
                reservationUserResponse.userName(),
                reservationUserResponse.name(),
                reservationUserResponse.lastname()
        );
    }

    @Override
    public ReservationResponse createExternReservation(ReservationExternRequest reservationRequest) {

        Set<String> roles = Set.of("EXTERNO");

        Optional<UserEntity> user = userEntityRepository.findByUsernameNoStudent(reservationRequest.username(), RoleName.ESTUDIANTE);

        if (user.isEmpty()) {
            UserRequest userRequest = new UserRequest(
                    reservationRequest.username(),
                    reservationRequest.name(),
                    reservationRequest.lastName(),
                    reservationRequest.email(),
                    null,
                    reservationRequest.plan(),
                    roles,
                    null
            );

            userService.createUser(userRequest);
        }

        UserEntity userEntity = userEntityRepository.findByUsernameNoStudent(reservationRequest.username(), RoleName.ESTUDIANTE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        ReservationUserResponse reservationUserResponse = createReservation(userEntity, reservationRequest.lunch(), reservationRequest.snack());
        return new ReservationResponse(
                reservationUserResponse.message(),
                reservationUserResponse.reservationId(),
                reservationUserResponse.date(),
                reservationUserResponse.time(),
                reservationUserResponse.paid(),
                reservationUserResponse.lunch(),
                reservationUserResponse.snack(),
                reservationUserResponse.userName(),
                reservationUserResponse.name(),
                reservationUserResponse.lastname()
        );
    }

    @Override
    public ExternResponse getExtern(String username) {
        UserEntity user = userEntityRepository.findByUsernameNoStudent(username, RoleName.ESTUDIANTE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return new ExternResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getPlan(),
                user.getEmail()
        );
    }

    @Override
    public AvailabilityResponse getAvailability() {

        LocalDate today = LocalDate.now();

        Optional<Setting> setting = settingRepository.findSettingById(1);

        if (setting.isEmpty()) {
            return new AvailabilityResponse(
                    0,
                    0
            );
        }

        int maxLunchSlots = setting.get().getNumLunch();
        int currentLunchReservations = reservationRepository.countLunchReservationsForDay(today);
        int remainingSlotsLunch = maxLunchSlots - currentLunchReservations;

        int maxSnackSlots = setting.get().getNumSnack();
        int currentSnackReservations = reservationRepository.countSnackReservationsForDay(today);
        int remainingSlotsSnack = maxSnackSlots - currentSnackReservations;

        return new AvailabilityResponse(
                remainingSlotsLunch,
                remainingSlotsSnack
        );
    }

    @Override
    public AvailabilityPerHourResponse getAvailabilityPerHour() {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        int availability = 0;
        LocalTime start = LocalTime.MIN;
        LocalTime end = LocalTime.MAX;
        String type = "";

        Optional<Setting> setting = settingRepository.findSettingById(1);

        if (setting.isPresent() && now.isAfter(setting.get().getStarBeneficiaryLunch()) && now.isBefore(setting.get().getStarBeneficiarySnack())) {
            int maxSlots = setting.get().getNumLunch();
            int currentReservations = reservationRepository.countLunchReservationsForDay(today);
            availability = maxSlots - currentReservations;
            start = setting.get().getStarBeneficiaryLunch();
            end = setting.get().getStarBeneficiarySnack();
            type = "Almuerzo";

        }
        if (setting.isPresent() && now.isAfter(setting.get().getStarBeneficiarySnack())){
            int maxSlots = setting.get().getNumSnack();
            int currentReservations = reservationRepository.countSnackReservationsForDay(today);
            availability = maxSlots - currentReservations;
            start = setting.get().getStarBeneficiarySnack();
            end = setting.get().getStarBeneficiaryLunch();
            type = "Refrigerio";
        }

        return new AvailabilityPerHourResponse(
                availability,
                start,
                end,
                type
        );
    }

    public void addReservationstoReservationResponse(List<ReservationResponse> list, Optional<Reservation> r) {
        r.ifPresent(reservation -> list.add(
                new ReservationResponse(
                        "Reserva encontrada",
                        reservation.getId(),
                        reservation.getData(),
                        reservation.getTime(),
                        reservation.getPaid(),
                        reservation.getLunch(),
                        reservation.getSnack(),
                        reservation.getUserEntity().getUsername(),
                        reservation.getUserEntity().getName(),
                        reservation.getUserEntity().getLastName()
                )
        ));
    }

    @Override
    public List<ReservationResponse> getReservationsPerDay(String username){
        LocalDate date = LocalDate.now();

        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Optional<Reservation> lunchReservation = reservationRepository.findLunchReservationPerDay(user, date);
        Optional<Reservation> snackReservation = reservationRepository.findSnackReservationPerDay(user, date);

        List<ReservationResponse> reservationResponses = new ArrayList<>();

        addReservationstoReservationResponse(reservationResponses, lunchReservation);
        addReservationstoReservationResponse(reservationResponses, snackReservation);

        return reservationResponses;
    }

    @Override
    public ReservationResponse cancelReservation(Integer reservationId) {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        Optional<Setting> setting = settingRepository.findSettingById(1);

        Reservation reservation = new Reservation();

        if (setting.isEmpty()) {
            throw new ResourceNotFoundException("Configuración no encontrada");
        }

        if (now.isBefore(setting.get().getStarBeneficiarySnack()) && now.isAfter(setting.get().getStarBeneficiaryLunch())) {
            reservation = reservationRepository.findLunchReservationById(reservationId, today)
                    .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene una reserva de almuerzo para cancelar el día de hoy"));
        }
        if (now.isAfter(setting.get().getStarBeneficiarySnack())){
            reservation = reservationRepository.findSnackReservationById(reservationId, today)
                    .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene una reserva de refrigerio para cancelar el día de hoy"));
        }

        Integer id = reservation.getId();
        LocalDateTime date = reservation.getData();
        LocalTime time = reservation.getTime();
        Boolean paid = reservation.getPaid();
        Boolean lunch = reservation.getLunch();
        Boolean snack = reservation.getSnack();
        String userName = reservation.getUserEntity().getUsername();
        String name = reservation.getUserEntity().getName();
        String lastName = reservation.getUserEntity().getLastName();

        reservationRepository.delete(reservation);

        return new ReservationResponse(
                "Reserva cancelada con éxito.",
                id,
                date,
                time,
                paid,
                lunch,
                snack,
                userName,
                name,
                lastName
        );
    }

    //buscar la reserva con el codigo del usuario
    @Override
    public ReservationResponse findReservationByUsername(String username) {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        List<Reservation> reservations = new ArrayList<>();

        Optional<Setting> setting = settingRepository.findSettingById(1);

        if (setting.isEmpty()) {
            throw new ResourceNotFoundException("Configuración no encontrada");
        }

        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (now.isBefore(setting.get().getStarBeneficiarySnack()) && now.isAfter(setting.get().getStarBeneficiaryLunch())) {
            reservations = reservationRepository.findByUserEntityLunchPaidFalse(user, today);
        }
        if (now.isAfter(setting.get().getStarBeneficiarySnack())){
            reservations = reservationRepository.findByUserEntitySnackPaidFalse(user, today);
        }

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
                user.getUsername(),
                user.getName(),
                user.getLastName()
        );
    }

    //registrar pago
    @Override
    public ReservationPaymentResponse registerPayment(ReservationPaymentRequest paymentRequest) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        UserEntity user = userEntityRepository.findByUsername(paymentRequest.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Optional<Setting> setting = settingRepository.findSettingById(1);

        List<Reservation> reservations = new ArrayList<>();
        List<Reservation> lunchReservations = reservationRepository.findByUserEntityLunchPaidFalse(user, today);
        List<Reservation> snackReservations = reservationRepository.findByUserEntitySnackPaidFalse(user, today);

        if (setting.isPresent() && lunchReservations.isEmpty() && now.isAfter(setting.get().getStarBeneficiaryLunch()) && now.isBefore(setting.get().getStarBeneficiarySnack())) {
            throw new ResourceNotFoundException("No se encontraron reservas de almuerzo pendientes para este usuario.");
        }

        if (setting.isPresent() && snackReservations.isEmpty() && now.isAfter(setting.get().getStarBeneficiarySnack())) {
            throw new ResourceNotFoundException("No se encontraron reservas de refrigerio pendientes para este usuario.");
        }

        if (setting.isPresent() && now.isAfter(setting.get().getStarBeneficiaryLunch()) && now.isBefore(setting.get().getStarBeneficiarySnack())) {
            reservations = lunchReservations;
        }

        if (setting.isPresent() && now.isAfter(setting.get().getStarBeneficiarySnack())) {
            reservations = snackReservations;
        }

        Reservation lastReservation = reservations.stream()
                .max(Comparator.comparing(Reservation::getData))
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva más reciente."));

        lastReservation.setPaid(paymentRequest.paid());
        reservationRepository.save(lastReservation);

        return new ReservationPaymentResponse("Pago registrado con éxito.", lastReservation.getId());
    }

    //tabla
    @Override
    public Page<ListReservationResponse> getActiveReservations(Pageable pageable) {
        LocalDate date = LocalDate.now();
        LocalTime now = LocalTime.now();

        Optional<Setting> setting = settingRepository.findSettingById(1);


        Page<ListReservationResponse> responses = Page.empty();

        // Verifcar en qué rango de tiempo estamos, según los ajustes en "setting"
        if (setting.isPresent() && now.isAfter(setting.get().getStarBeneficiaryLunch()) && now.isBefore(setting.get().getStarBeneficiarySnack())) {
            // Caso para reservas de almuerzo no pagadas
            responses = reservationRepository.findAllLunchByPaidFalse(pageable, date)
                    .map(reservation -> new ListReservationResponse(
                            reservation.getId(),
                            reservation.getData(),
                            reservation.getTime(),
                            reservation.getPaid(),
                            reservation.getSnack(),
                            reservation.getLunch(),
                            reservation.getUserEntity().getUsername(),
                            reservation.getUserEntity().getName(),
                            reservation.getUserEntity().getLastName()
                    ));
        }

        if (setting.isPresent() && now.isAfter(setting.get().getStarBeneficiarySnack())) {
            // Caso para reservas de refrigerio no pagadas
            responses = reservationRepository.findAllSnackByPaidFalse(pageable, date)
                    .map(reservation -> new ListReservationResponse(
                            reservation.getId(),
                            reservation.getData(),
                            reservation.getTime(),
                            reservation.getPaid(),
                            reservation.getSnack(),
                            reservation.getLunch(),
                            reservation.getUserEntity().getUsername(),
                            reservation.getUserEntity().getName(),
                            reservation.getUserEntity().getLastName()
                    ));
        }

        return responses;
    }


}
