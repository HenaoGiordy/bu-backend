package com.univalle.bubackend.controllers.nursing;

import com.univalle.bubackend.DTOs.nursing.NursingReportRequest;
import com.univalle.bubackend.DTOs.nursing.NursingReportResponse;
import com.univalle.bubackend.services.report.nursing.NursingReportServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nursing")
@AllArgsConstructor
public class NursingReportController {

    private NursingReportServiceImpl nursingReportService;

    @PostMapping("/report")
    public ResponseEntity<NursingReportResponse> generateNursingReport(@RequestBody NursingReportRequest nursingReportRequest) {
        return new ResponseEntity<>(nursingReportService.generateNursingReport(nursingReportRequest), HttpStatus.CREATED);
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<NursingReportResponse> getNursingReport(@PathVariable int id) {
        return new ResponseEntity<>(nursingReportService.getNursingReport(id), HttpStatus.OK);
    }


}
