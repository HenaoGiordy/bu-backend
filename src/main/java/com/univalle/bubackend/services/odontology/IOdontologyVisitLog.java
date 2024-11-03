package com.univalle.bubackend.services.odontology;

import com.univalle.bubackend.DTOs.odontology.*;
import org.springframework.data.domain.Pageable;

public interface IOdontologyVisitLog {
    UserResponse findStudentsByUsername(String username);

    VisitLogResponse registerVisit(VisitLogRequest request);

    VisitOdontologyResponse visitsOdonotology(String username, Pageable pageable);

    VisitResponse getOdontologyVisit(Long id);
}
