package com.univalle.bubackend.services.odontology;

import com.univalle.bubackend.DTOs.odontology.UserResponse;
import com.univalle.bubackend.DTOs.odontology.VisitLogRequest;
import com.univalle.bubackend.DTOs.odontology.VisitLogResponse;

public interface IOdontologyVisitLog {
    UserResponse findStudentsByUsername(String username);

    VisitLogResponse registerVisit(VisitLogRequest request);
}
