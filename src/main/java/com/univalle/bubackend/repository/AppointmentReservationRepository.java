package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.AppointmentReservation;
import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentReservationRepository extends JpaRepository<AppointmentReservation, Integer> {
    Optional<List<AppointmentReservation>> findByAvailableDates_ProfessionalId(Integer id);
    Optional<List<AppointmentReservation>> findByEstudiante_Id(Integer id);

    Optional<AppointmentReservation> findByEstudiante_IdAndPendingAppointmentTrue(Integer id);
}
