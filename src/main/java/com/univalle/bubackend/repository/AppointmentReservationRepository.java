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

    Optional<AppointmentReservation> findByEstudiante_IdAndPendingAppointmentTrue(Integer id);

    @Query("SELECT COUNT(a) FROM AppointmentReservation a WHERE a.estudiante = :id AND a.availableDates.typeAppointment = :typeAppointmern AND a.availableDates.dateTime BETWEEN :startDate AND :endDate")
    Integer countAppointmentReservationByEstudiante_IdAndAvailableDates_DateTime(
            @Param("id") UserEntity id,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("typeAppointmern") TypeAppointment typeAppointmern);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN AppointmentReservation ar ON ar.estudiante = u " +
            "JOIN ar.availableDates ad " +
            "WHERE u.username = :username " +
            "AND ad.professional.username = :usernameProfesional AND ar.availableDates.dateTime BETWEEN :startDate AND :endDate")
    Optional<UserEntity> findByUsernameWithPsychoReservation(
            @Param("username") String username,
            @Param("usernameProfesional") String usernameProfesional,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
            );

    @Query("SELECT a FROM AppointmentReservation  a")
    Page<AppointmentReservation> getAllAppointmentReservation(Pageable pageable);
}
