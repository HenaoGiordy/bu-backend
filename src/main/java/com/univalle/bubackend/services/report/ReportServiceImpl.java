package com.univalle.bubackend.services.report;

import com.univalle.bubackend.DTOs.report.UserDTO;
import com.univalle.bubackend.models.Report;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl {
    private final UserEntityRepository userEntityRepository;

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

}
