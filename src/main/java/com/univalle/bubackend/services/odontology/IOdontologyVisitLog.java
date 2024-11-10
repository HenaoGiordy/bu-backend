package com.univalle.bubackend.services.odontology;

import com.univalle.bubackend.DTOs.odontology.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IOdontologyVisitLog {
    UserResponse findStudentsByUsername(String username);

    VisitLogResponse registerVisit(VisitLogRequest request);

    VisitOdontologyResponse visitsOdonotology(String username, LocalDate startDate, LocalDate endDate, Pageable pageable);

    VisitResponse getOdontologyVisit(Long id);
}
