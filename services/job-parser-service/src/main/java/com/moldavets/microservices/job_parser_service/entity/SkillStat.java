package com.moldavets.microservices.job_parser_service.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "skill_stats")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SkillStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer id;

    @Column(name = "skill_name")
    String skillName;

    @Column(name = "count")
    Integer count;

    @Column(name = "tech")
    String tech;

    @Column(name = "level")
    String level;

    @Column(name = "last_update")
    LocalDate lastUpdate;
}
