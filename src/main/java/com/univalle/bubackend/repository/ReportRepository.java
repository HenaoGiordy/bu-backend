package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    Optional<Report> findByDate(LocalDate date);
    Optional<Report> findById(Integer id);
    List<Report> findBySemester(String semester);
    List<Report> findAllByDate(LocalDate date);
}
