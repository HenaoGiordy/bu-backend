package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.NursingActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NursingActivityRepository extends JpaRepository<NursingActivityLog, Integer> {
}
