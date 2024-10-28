package com.univalle.bubackend.services.report.allowance;

import com.univalle.bubackend.DTOs.report.ReportRequest;
import com.univalle.bubackend.DTOs.report.ReportResponse;
import com.univalle.bubackend.DTOs.report.UserDTO;
import com.univalle.bubackend.exceptions.users.InvalidFilter;
import com.univalle.bubackend.exceptions.report.SettingNotFound;
import com.univalle.bubackend.exceptions.report.BecaInvalid;
import com.univalle.bubackend.exceptions.report.ReportNotFound;
import com.univalle.bubackend.models.Report;
import com.univalle.bubackend.models.Setting;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.ReportRepository;
import com.univalle.bubackend.repository.SettingRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl {
    private final UserEntityRepository userEntityRepository;
    private final ReportRepository reportRepository;
    private final SettingRepository settingRepository;

    public Report generateReport(ReportRequest reportRequest) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<UserEntity> filterUsers;

        // Filtrar usuarios según la beca
        if ("almuerzo".equalsIgnoreCase(reportRequest.beca())) {
            filterUsers = userEntityRepository.findUserLunchPaid(startOfDay, endOfDay);
        } else if ("refrigerio".equalsIgnoreCase(reportRequest.beca())) {
            filterUsers = userEntityRepository.findUserSnackPaid(startOfDay, endOfDay);
        } else {
            throw new BecaInvalid("Tipo de beca no válida");
        }

        // Inicializar el reporte
        Report report = new Report();
        report.setDate(today);
        report.setBeca(reportRequest.beca());
        report.setSemester(reportRequest.semester()); // Si es null, será un reporte diario
        report.setUserEntities(new HashSet<>(filterUsers));

        Map<Integer, Integer> countReports = new HashMap<>();

        // Si el semestre no es null, estamos generando un reporte semestral
        if (reportRequest.semester() != null) {
            // Obtener la configuración del semestre (fechas de inicio y fin)
            Setting setting = settingRepository.findTopByOrderByIdAsc()
                    .orElseThrow(() -> new SettingNotFound("Ajuste no encontrado"));

            LocalDate startSemester = setting.getStartSemester();
            LocalDate endSemester = setting.getEndSemester();

            // Filtrar y contar cuántas veces cada usuario apareció en reportes diarios dentro del semestre actual
            for (UserEntity user : filterUsers) {
                long count = user.getReports().stream()
                        .filter(r -> r.getBeca().equalsIgnoreCase(reportRequest.beca()) &&  // Tipo de beca coincide
                                r.getSemester() == null &&                               // Solo reportes diarios (semestre null)
                                !r.getDate().isBefore(startSemester) &&                  // Reporte dentro del rango del semestre
                                !r.getDate().isAfter(endSemester))                       // Reporte dentro del rango del semestre
                        .count();

                // Almacenamos el conteo en el mapa solo para este semestre
                countReports.put(user.getId(), (int) count);
            }
        }

        // Guardar el conteo de reportes en el reporte semestral
        report.setUserReportCount(countReports);
        report = reportRepository.save(report);  // Guardar el reporte en la base de datos

        // Guardar el reporte en la lista de reportes del usuario
        for (UserEntity user : filterUsers) {
            user.getReports().add(report);
            userEntityRepository.save(user);
        }

        return report;
    }



    public void deleteReport(Integer id) {
        if (!reportRepository.existsById(id)) {
            throw new ReportNotFound("Informe no encontrado");
        }
        reportRepository.deleteById(id);
    }

    public ByteArrayInputStream generateExcelReport(Integer id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFound("Informe no encontrado"));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Informe");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Fecha");
            headerRow.createCell(1).setCellValue(report.getDate().toString());
            headerRow.createCell(2).setCellValue("Tipo de beca");
            headerRow.createCell(3).setCellValue(report.getBeca());

            if (report.getSemester() != null) {
                headerRow.createCell(4).setCellValue("Semestre: " + report.getSemester());
            }

            Row userHeader = sheet.createRow(2);
            userHeader.createCell(0).setCellValue("Codigo/Cedula");
            userHeader.createCell(1).setCellValue("Nombre");
            userHeader.createCell(2).setCellValue("Plan/Area");
            userHeader.createCell(3).setCellValue("Correo");

            int countCellIndex = 4;
            if (report.getSemester() != null) {
                userHeader.createCell(countCellIndex).setCellValue("Cantidad de " + report.getBeca());
            }

            int rowNum = 3;
            for (UserEntity user : report.getUserEntities()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getUsername());
                row.createCell(1).setCellValue(user.getName() + " " + user.getLastName());
                row.createCell(2).setCellValue(user.getPlan());
                row.createCell(3).setCellValue(user.getEmail());

                if (report.getSemester() != null) {
                    int count = report.getUserReportCount().getOrDefault(user.getId(), 0);
                    row.createCell(countCellIndex).setCellValue(count);
                }

            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }

    public List<Report> findReportsBySemester(String semester) {
        return reportRepository.findBySemester(semester);
    }

    public List<Report> findReportsByDate(LocalDate date) {
        return reportRepository.findAllByDate(date);
    }

    public ReportResponse viewReport(Integer id){
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFound("Informe no encontrado"));

        List<UserDTO> users = report.getUserEntities().stream()
                .map(user -> {
                    int count = report.getUserReportCount().getOrDefault(user.getId(), 0);

                    return new UserDTO(
                            user.getUsername(),
                            user.getName() + " " + user.getLastName(),
                            user.getEmail(),
                            user.getPlan(),
                            user.getRoles(),
                            user.getIsActive(),
                            user.getLunchBeneficiary(),
                            user.getSnackBeneficiary(),
                            count
                    );
                })
                .collect(Collectors.toList());

        return new ReportResponse(report.getId(), report.getDate(), report.getSemester(), report.getBeca(), users);

    }

    public Page<ReportResponse> listReports(String filter, Pageable pageable) {
        Page<Report> reports;

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        switch (filter.toLowerCase()) {
            case "diario":
                reports = reportRepository.findDailyReports(sortedPageable);
                break;
            case "semester":
                reports = reportRepository.findSemesterReports(sortedPageable);
                break;
            default:
                throw new InvalidFilter("Filtro no válido");
        }

        return reports.map(report -> ReportResponse.builder()
                .beca(report.getBeca())
                .date(report.getDate())
                .id(report.getId())
                .semester(report.getSemester())
                .build()
        );

    }

}
