package com.univalle.bubackend.services.report.nursing;

import com.univalle.bubackend.DTOs.nursing.NursingReportRequest;
import com.univalle.bubackend.DTOs.nursing.NursingReportResponse;

public interface INursingReportService {
    NursingReportResponse generateNursingReport(NursingReportRequest request);
    NursingReportResponse getNursingReport(Integer id);
    void deleteNursingReport(Integer id);
}
