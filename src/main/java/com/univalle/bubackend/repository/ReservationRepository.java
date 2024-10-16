package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.Reservation;
import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.lunch = true AND DATE(r.data) = :date")
    int countLunchReservationsForDay(LocalDate date);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.snack = true AND DATE(r.data) = :date")
    int countSnackReservationsForDay(LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.userEntity = :userEntity AND r.paid = false AND r.lunch = true")
    List<Reservation> findByUserEntityLunchPaidFalse(UserEntity userEntity);

    @Query("SELECT r FROM Reservation r WHERE r.userEntity = :userEntity AND r.paid = false AND r.snack = true")
    List<Reservation> findByUserEntitySnackPaidFalse(UserEntity userEntity);

    @Query("SELECT r FROM Reservation r Where r.userEntity = :userEntity AND DATE(r.data) = :date AND r.snack = true")
    Optional<Reservation> findSnackReservationPerDay(UserEntity userEntity, LocalDate date);

    @Query("SELECT r FROM Reservation r Where r.userEntity = :userEntity AND DATE(r.data) = :date AND r.lunch = true")
    Optional<Reservation> findLunchReservationPerDay(UserEntity userEntity, LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.paid = false AND DATE(r.data) = :date AND r.lunch = true")
    Page<Reservation> findAllLunchByPaidFalse(Pageable pageable, LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.paid = false AND DATE(r.data) = :date AND r.snack = true")
    Page<Reservation> findAllSnackByPaidFalse(Pageable pageable, LocalDate date);
}
