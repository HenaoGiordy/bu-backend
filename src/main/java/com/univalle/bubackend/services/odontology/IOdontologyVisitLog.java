package com.univalle.bubackend.services.odontology;

import com.univalle.bubackend.DTOs.odontology.VisitLogRequest;
import com.univalle.bubackend.DTOs.odontology.VisitLogResponse;

public interface IOdontologyVisitLog {
    VisitLogResponse registerVisit(VisitLogRequest request);
}
