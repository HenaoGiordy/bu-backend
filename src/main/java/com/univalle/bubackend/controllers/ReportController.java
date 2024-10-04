package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.report.DeleteResponse;
import com.univalle.bubackend.services.report.ReportServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "Security Token")
public class ReportController {
    private final ReportServiceImpl reportService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteReport(@PathVariable Integer id) {
        reportService.deleteReport(id);
        return new ResponseEntity<>(new DeleteResponse("Informe eliminado correctamente."), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable Integer id) {
        ByteArrayInputStream excelStream = reportService.generateExcelReport(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + id + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelStream));
    }



}
