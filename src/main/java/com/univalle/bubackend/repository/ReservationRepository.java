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

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.data = :date AND r.lunch = :isLunch")
    int countRemainingSlotsForDay(LocalDate date, boolean isLunch);

    @Query("SELECT r FROM Reservation r WHERE r.userEntity = :userEntity AND r.paid = false")
    List<Reservation> findByUserEntityAndPaidFalse(UserEntity userEntity);
}
