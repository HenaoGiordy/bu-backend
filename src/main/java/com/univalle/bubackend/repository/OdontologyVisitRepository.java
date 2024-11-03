package com.univalle.bubackend.repository;

import com.univalle.bubackend.DTOs.odontology.VisitResponse;
import com.univalle.bubackend.models.VisitOdontologyLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OdontologyVisitRepository extends JpaRepository<VisitOdontologyLog, Integer> {
    Page<VisitResponse> findAllByUserUsername(String username, Pageable pageable);
    VisitOdontologyLog findById(Long id);
}
