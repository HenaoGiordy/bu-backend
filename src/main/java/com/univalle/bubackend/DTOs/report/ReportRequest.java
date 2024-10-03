package com.univalle.bubackend.DTOs.report;

import java.time.LocalDate;
import java.util.List;

public record ReportRequest(Integer id, String name, LocalDate date, String semester, List<UserDTO> users) {
}
