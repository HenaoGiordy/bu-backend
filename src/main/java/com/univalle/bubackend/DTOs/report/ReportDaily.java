package com.univalle.bubackend.DTOs.report;

import java.time.LocalDate;
import java.util.List;

public record ReportDaily(Integer id, String name, LocalDate date, String beca, List<UserDTO> users) {
}
