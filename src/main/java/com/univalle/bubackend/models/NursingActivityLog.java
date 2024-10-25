package com.univalle.bubackend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NursingActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private LocalTime time;

    private Diagnostic diagnostic;

    private String conduct;

}
