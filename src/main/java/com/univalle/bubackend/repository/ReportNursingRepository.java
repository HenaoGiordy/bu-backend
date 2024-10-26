package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.NursingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportNursingRepository extends JpaRepository<NursingReport, Integer> {
}
