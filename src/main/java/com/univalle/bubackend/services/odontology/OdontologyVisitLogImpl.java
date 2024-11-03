package com.univalle.bubackend.services.odontology;

import com.univalle.bubackend.DTOs.odontology.*;
import com.univalle.bubackend.exceptions.ResourceNotFoundException;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.models.VisitOdontologyLog;
import com.univalle.bubackend.repository.OdontologyVisitRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class OdontologyVisitLogImpl implements IOdontologyVisitLog {

    private final UserEntityRepository userEntityRepository;
    private final OdontologyVisitRepository odontologyVisitRepository;

    @Override
    public UserResponse findStudentsByUsername(String username) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);
        UserEntity user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return new UserResponse(user);
    }

    @Override
    public VisitLogResponse registerVisit(VisitLogRequest request) {
        UserEntity user = userEntityRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        VisitOdontologyLog visitOdontologyLog = VisitOdontologyLog.builder()
                .user(user)
                .date(request.date())
                .reason(request.reason())
                .description(request.description())
                .build();

        odontologyVisitRepository.save(visitOdontologyLog);

        return new VisitLogResponse(
                visitOdontologyLog.getId(),
                visitOdontologyLog.getDate().toLocalDate(),
                visitOdontologyLog.getDate().toLocalTime(),
                user.getUsername(),
                user.getName() + " " + user.getLastName(),
                user.getPlan(),
                visitOdontologyLog.getReason(),
                visitOdontologyLog.getDescription()
        );
    }

    @Override
    public VisitOdontologyResponse visitsOdonotology(String username, Pageable pageable) {
        UserEntity usr = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        UserResponse user = new UserResponse(usr);

        Page<VisitResponse> list = Page.empty();

        list = odontologyVisitRepository.findAllByUserUsername(username, pageable)
                .map(visit -> new VisitResponse(
                        visit.date(),
                        visit.reason(),
                        visit.description()
                ));

        return new VisitOdontologyResponse(list, user);
    }

    @Override
    public VisitResponse getOdontologyVisit(Long id) {
        VisitOdontologyLog visit = odontologyVisitRepository.findById(id);
        UserResponse user = new UserResponse(visit.getUser());
        return new VisitResponse(
                visit.getDate(),
                visit.getReason(),
                visit.getDescription()
        );
    }

}
