package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.NursingActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NursingActivityRepository extends JpaRepository<NursingActivityLog, Integer> {
    List<NursingActivityLog> findAllByUserUsername(String username);
    Optional<NursingActivityLog> findById(Long id);

}
