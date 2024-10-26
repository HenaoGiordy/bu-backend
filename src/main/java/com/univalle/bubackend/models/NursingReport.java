package com.univalle.bubackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NursingReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;

    private int trimester;

    private int year;

    @ElementCollection
    @CollectionTable(name = "diagnostic_count", joinColumns = @JoinColumn(name = "nursing_report_id"))
    @MapKeyColumn(name = "diagnostic")
    @Column(name = "count")
    private Map<Diagnostic, Integer> diagnosticCounts = new HashMap<>();

    @OneToMany
    @JoinColumn(name = "nursing_report_id")
    private List<NursingActivityLog> activities = new ArrayList<>();


}
