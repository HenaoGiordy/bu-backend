package com.univalle.bubackend.DTOs.report;

import java.time.LocalDate;
import java.util.List;

public record ReportRequest(String semester, String beca, List<UserDTO> users) {
}
