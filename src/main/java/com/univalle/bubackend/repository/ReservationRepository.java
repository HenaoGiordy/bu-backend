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

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT s.numLunch FROM Setting s")
    int getMaxRemainingLunchSlots();

    @Query("SELECT s.numSnack FROM Setting s")
    int getMaxRemainingSnackSlots();

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.lunch = TRUE AND r.data = :today")
    int countLunchReservationsForDay(LocalDate today);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.snack = TRUE AND r.data = :today")
    int countSnackReservationsForDay(LocalDate today);

    @Query("SELECT r FROM Reservation r WHERE r.userEntity = :userEntity AND r.paid = false")
    List<Reservation> findByUserEntityAndPaidFalse(UserEntity userEntity);

    @Query("SELECT r FROM Reservation r Where r.userEntity = :userEntity AND DATE(r.data) = :date")
    List<Reservation> findReservationsPerDay(UserEntity userEntity, LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.paid = false AND DATE(r.data) = :date")
    Page<Reservation> findAllByPaidFalse(Pageable pageable, LocalDate date);


}
