package com.univalle.bubackend.services.report;

import com.univalle.bubackend.DTOs.report.ReportDaily;
import com.univalle.bubackend.DTOs.report.UserDTO;
import com.univalle.bubackend.exceptions.report.ReportNotFound;
import com.univalle.bubackend.models.Report;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.ReportRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl {
    private final UserEntityRepository userEntityRepository;
    private final ReportRepository reportRepository;

    public List<UserDTO> generateReport() {
        LocalDateTime today = LocalDateTime.now();
        List<UserEntity> users = userEntityRepository.findAllByReservations_PaidAndDate(true, today);

        return users.stream()
                .map(user -> new UserDTO(
                        user.getUsername(),
                        user.getName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPlan(),
                        user.getRoles()
                ))
                .collect(Collectors.toList());

    }

    public String generateReportName(Report report) {
        return report.getBeca() + " " + report.getId();
    }

    public LocalDate getReportDate() {
        return LocalDate.now();
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
            XSSFSheet sheet = workbook.createSheet("Reporte");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nombre del reporte");
            headerRow.createCell(1).setCellValue(report.getName());
            headerRow.createCell(2).setCellValue("Fecha");
            headerRow.createCell(3).setCellValue(report.getDate().toString());
            headerRow.createCell(4).setCellValue("Tipo de beca");
            headerRow.createCell(5).setCellValue(report.getBeca());

            Row userHeader = sheet.createRow(2);
            userHeader.createCell(0).setCellValue("Codigo/Cedula");
            userHeader.createCell(1).setCellValue("Nombre");
            userHeader.createCell(2).setCellValue("Plan/Area");
            userHeader.createCell(3).setCellValue("Correo");

            int rowNum = 3;
            for (UserEntity user : report.getUserEntities()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getUsername());
                row.createCell(1).setCellValue(user.getName() + " " + user.getLastName());
                row.createCell(2).setCellValue(user.getPlan());
                row.createCell(3).setCellValue(user.getEmail());
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

    public ReportDaily reportDaily(Integer id){
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFound("Informe no encontrado"));

        List<UserDTO> users = report.getUserEntities().stream()
                .map(user -> new UserDTO(
                        user.getUsername(),
                        user.getName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPlan(),
                        user.getRoles()
                )).toList();

        return new ReportDaily(report.getId(), report.getName(), report.getDate(), report.getBeca(), users);

    }



}
