package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.nursing.ActivityLogRequest;
import com.univalle.bubackend.DTOs.nursing.ActivityLogResponse;
import com.univalle.bubackend.DTOs.nursing.UserResponse;
import com.univalle.bubackend.services.nursing.NursingActivityLogImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nursing-activities")
@AllArgsConstructor
public class NursingActivityLogController {

    private NursingActivityLogImpl nursingActivityLog;

    @GetMapping("/search/{username}")
    public ResponseEntity<UserResponse> searchStudentsByCode(@PathVariable String username) {
        UserResponse response = nursingActivityLog.findStudentsByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<ActivityLogResponse> registerActivity(@RequestBody ActivityLogRequest request) {
        ActivityLogResponse response = nursingActivityLog.registerActivity(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
