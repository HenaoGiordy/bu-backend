package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.AvailableDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableDatesRepository extends JpaRepository<AvailableDates, Integer> {
}
