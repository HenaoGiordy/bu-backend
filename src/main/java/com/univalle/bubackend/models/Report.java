package com.univalle.bubackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String beca;

    @Column(nullable = false)
    private LocalDate date;

    private String semester;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private Set<UserEntity> userEntities; // no permite duplicados y no importa el orden en que son añadidos

  //  private Set<UserEntity> userEntities = new LinkedHashSet<>(); este no permite duplicados y los añade en orden



}