package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.AvailableDates;
import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvailableDatesRepository extends JpaRepository<AvailableDates, Integer> {
    Optional<List<AvailableDates>> findByProfessionalId(Integer professionalId);
}
