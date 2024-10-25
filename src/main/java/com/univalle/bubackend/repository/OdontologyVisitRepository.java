package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.VisitOdontologyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OdontologyVisitRepository extends JpaRepository<VisitOdontologyLog, Integer> {
}
