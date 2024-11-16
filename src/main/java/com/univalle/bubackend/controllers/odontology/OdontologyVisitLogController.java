package com.univalle.bubackend.controllers.odontology;


import com.univalle.bubackend.DTOs.odontology.*;
import com.univalle.bubackend.services.odontology.OdontologyVisitLogImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/odontology-visits")
@AllArgsConstructor
public class OdontologyVisitLogController {

    private OdontologyVisitLogImpl odontologyVisitLog;

    @GetMapping("/search/{username}")
    public ResponseEntity<UserResponse> searchStudentsByCode(@PathVariable String username) {
        UserResponse response = odontologyVisitLog.findStudentsByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
        
    @PostMapping("/register")
    public ResponseEntity<VisitLogResponse> registerActivity(@RequestBody VisitLogRequest request) {
        VisitLogResponse response = odontologyVisitLog.registerVisit(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<VisitOdontologyResponse> getVisitLog(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, page = 0) Pageable pageable){
        return new ResponseEntity<>(odontologyVisitLog.visitsOdonotology(username, startDate, endDate, pageable), HttpStatus.OK);
    }

    @GetMapping("/visit/{id}")
    public ResponseEntity<VisitResponse> getVisit(@PathVariable Long id) {
        return new ResponseEntity<>(odontologyVisitLog.getOdontologyVisit(id), HttpStatus.OK);
    }
}
