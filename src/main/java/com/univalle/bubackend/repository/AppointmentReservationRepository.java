package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.AppointmentReservation;
import com.univalle.bubackend.models.TypeAppointment;
import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentReservationRepository extends JpaRepository<AppointmentReservation, Integer> {
    Optional<List<AppointmentReservation>> findByAvailableDates_ProfessionalId(Integer id);
    Optional<List<AppointmentReservation>> findByEstudiante_Id(Integer id);

    Optional<AppointmentReservation> findByEstudiante_IdAndPendingAppointmentTrueAndAvailableDates_TypeAppointment(Integer id, TypeAppointment type);

    @Query("SELECT COUNT(a) FROM AppointmentReservation a " +
            "WHERE a.estudiante = :id AND " +
            "a.availableDates.typeAppointment = :typeAppointment AND " +
            "a.assistant = false AND " +
            "a.availableDates.dateTime BETWEEN :startDate AND :endDate")
    Integer countAppointmentReservationByEstudiante_IdAndAvailableDates_DateTime(
            @Param("id") UserEntity id,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("typeAppointment") TypeAppointment typeAppointment);


    @Query("SELECT u FROM UserEntity u " +
            "JOIN AppointmentReservation ar ON ar.estudiante = u " +
            "JOIN ar.availableDates ad " +
            "WHERE u.username = :username " +
            "AND ad.typeAppointment = :typeAppointment " +
            "AND ar.availableDates.dateTime BETWEEN :startDate AND :endDate")
    Optional<UserEntity> findByUsernameWithPsychoReservation(
            @Param("username") String username,
            @Param("typeAppointment") TypeAppointment typeAppointment,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query("SELECT a FROM AppointmentReservation a Where a.estudiante.username = :username")
    Page<AppointmentReservation> getAllAppointmentReservationByUsername(Pageable pageable, String username);
}
