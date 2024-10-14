package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.AvailableDates;
import com.univalle.bubackend.models.TypeAppointment;
import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailableDatesRepository extends JpaRepository<AvailableDates, Integer> {
    Optional<List<AvailableDates>> findByProfessionalId(Integer professionalId);

    @Query("SELECT e FROM AvailableDates e WHERE e.dateTime BETWEEN :fechaInicio AND :fechaFin")
    Optional<List<AvailableDates>> findEventosWithin30Minutes(@Param("fechaInicio") LocalDateTime fechaInicio,
                                            @Param("fechaFin") LocalDateTime fechaFin);

    Optional<List<AvailableDates>> findByTypeAppointmentAndAvailableTrue(TypeAppointment typeAppointment);
}
