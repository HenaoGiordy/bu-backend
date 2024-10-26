package com.univalle.bubackend.controllers.nursing;

import com.univalle.bubackend.DTOs.nursing.NursingReportRequest;
import com.univalle.bubackend.DTOs.nursing.NursingReportResponse;
import com.univalle.bubackend.services.report.nursing.NursingReportServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nursing")
@AllArgsConstructor
public class NursingReportController {

    private NursingReportServiceImpl nursingReportService;

    @PostMapping("/report")
    public ResponseEntity<NursingReportResponse> generateNursingReport(@RequestBody NursingReportRequest nursingReportRequest) {
        return new ResponseEntity<>(nursingReportService.generateNursingReport(nursingReportRequest), HttpStatus.CREATED);
    }


}
