package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.report.DeleteResponse;
import com.univalle.bubackend.repository.ReportRepository;
import com.univalle.bubackend.services.report.ReportServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "Security Token")
public class ReportController {
    private final ReportServiceImpl reportService;

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteReport(@PathVariable Integer id) {
        reportService.deleteReport(id);
        return new ResponseEntity<>(new DeleteResponse("Informe eliminado correctamente."), HttpStatus.OK);
    }


}
