package com.univalle.bubackend.DTOs.report;

import java.time.LocalDate;
import java.util.List;

public record ReportResponse(Integer id, LocalDate date, String semester, String beca, List<UserDTO> users) {
}

