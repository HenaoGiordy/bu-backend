package com.univalle.bubackend.prueba;

import com.univalle.bubackend.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT r FROM Reservation r WHERE r.lunch = true AND r.paid = true AND r.data BETWEEN :startOfDay AND :endOfDay")
    List<Reservation> findPaidLunchReservations(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT r FROM Reservation r WHERE r.snack = true AND r.paid = true AND r.data BETWEEN :startOfDay AND :endOfDay")
    List<Reservation> findPaidSnackReservations(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
