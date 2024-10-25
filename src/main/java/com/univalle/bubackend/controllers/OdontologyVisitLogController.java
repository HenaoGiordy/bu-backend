package com.univalle.bubackend.controllers;


import com.univalle.bubackend.DTOs.odontology.VisitLogRequest;
import com.univalle.bubackend.DTOs.odontology.VisitLogResponse;
import com.univalle.bubackend.services.odontology.OdontologyVisitLogImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/odontology-visits")
@AllArgsConstructor
public class OdontologyVisitLogController {

        private OdontologyVisitLogImpl odontologyVisitLog;

    @PostMapping("/register")
    public ResponseEntity<VisitLogResponse> registerActivity(@RequestBody VisitLogRequest request) {
        VisitLogResponse response = odontologyVisitLog.registerVisit(request);
        return ResponseEntity.ok(response);
    }
}
