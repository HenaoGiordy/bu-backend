package com.univalle.bubackend.services.report.nursing;

import com.univalle.bubackend.DTOs.nursing.NursingReportRequest;
import com.univalle.bubackend.DTOs.nursing.NursingReportResponse;
import com.univalle.bubackend.models.NursingReport;

import java.util.List;

public interface INursingReportService {
    NursingReportResponse generateNursingReport(NursingReportRequest request);
    NursingReportResponse getNursingReport(Integer id);
    void deleteNursingReport(Integer id);
    List<NursingReport> findNursingReports(Integer year, Integer trimester);
}
