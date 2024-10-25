package com.univalle.bubackend.services.nursing;

import com.univalle.bubackend.DTOs.nursing.ActivityLogRequest;
import com.univalle.bubackend.DTOs.nursing.ActivityLogResponse;

public interface INursingActivityLog {
    ActivityLogResponse registerActivity(ActivityLogRequest request);
}
