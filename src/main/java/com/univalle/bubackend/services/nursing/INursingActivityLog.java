package com.univalle.bubackend.services.nursing;

import com.univalle.bubackend.DTOs.nursing.ActivityLogRequest;
import com.univalle.bubackend.DTOs.nursing.ActivityLogResponse;
import com.univalle.bubackend.DTOs.nursing.UserResponse;

public interface INursingActivityLog {
    UserResponse findStudentsByUsername(String username);
    ActivityLogResponse registerActivity(ActivityLogRequest request);
}
