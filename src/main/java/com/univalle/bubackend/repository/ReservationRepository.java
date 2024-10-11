package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.Reservation;
import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT COUNT(r) FROM Reservation r WHERE DATE(r.data) = :date AND r.lunch = :isLunch")
    int countRemainingLunchSlotsForDay(LocalDate date, boolean isLunch);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE DATE(r.data) = :date AND r.snack = :isSnack")
    int countRemainingSnacklotsForDay(LocalDate date, boolean isSnack);

    @Query("SELECT r FROM Reservation r WHERE r.userEntity = :userEntity AND r.paid = false")
    List<Reservation> findByUserEntityAndPaidFalse(UserEntity userEntity);

    @Query("SELECT r FROM Reservation r WHERE r.paid = false")
    List<Reservation> findAllByPaidFalse();

}
