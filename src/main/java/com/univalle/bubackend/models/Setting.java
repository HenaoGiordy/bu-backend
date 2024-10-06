package com.univalle.bubackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private LocalDate startSemester;

    @NotNull
    private LocalDate endSemester;

    @Positive
    private Integer numLunch;

    @Positive
    private Integer numSnack;

    @NotNull
    private LocalTime starBeneficiaryLunch;

    @NotNull
    private LocalTime endBeneficiaryLunch;

    @NotNull
    private LocalTime starLunch;

    @NotNull
    private LocalTime endLunch;

    @NotNull
    private LocalTime starBeneficiarySnack;

    @NotNull
    private LocalTime endBeneficiarySnack;

    @NotNull
    private LocalTime starSnack;

    @NotNull
    private LocalTime endSnack;



}
