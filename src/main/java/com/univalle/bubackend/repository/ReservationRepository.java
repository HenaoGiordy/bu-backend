package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.Reservation;
import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    int countRemainingSlotsForDay(LocalDate date, boolean isLunch);

    List<Reservation> findByUserEntityAndPaidFalse(UserEntity userEntity);
}
