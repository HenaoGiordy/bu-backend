package com.univalle.bubackend.controllers.nursing;

import com.univalle.bubackend.DTOs.nursing.NursingReportRequest;
import com.univalle.bubackend.DTOs.nursing.NursingReportResponse;
import com.univalle.bubackend.DTOs.report.DeleteResponse;
import com.univalle.bubackend.services.report.nursing.NursingReportServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nursing-report")
@AllArgsConstructor
public class NursingReportController {

    private NursingReportServiceImpl nursingReportService;

    @PostMapping
    public ResponseEntity<NursingReportResponse> generateNursingReport(@RequestBody NursingReportRequest nursingReportRequest) {
        return new ResponseEntity<>(nursingReportService.generateNursingReport(nursingReportRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NursingReportResponse> getNursingReport(@PathVariable int id) {
        return new ResponseEntity<>(nursingReportService.getNursingReport(id), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<DeleteResponse> deleteNursingReport(@PathVariable int id) {
        nursingReportService.deleteNursingReport(id);
        return new ResponseEntity<>(new DeleteResponse("Informe de enfermeria eliminado correctamente"), HttpStatus.OK);
    }


}
