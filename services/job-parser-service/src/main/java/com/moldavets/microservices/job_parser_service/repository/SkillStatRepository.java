package com.moldavets.microservices.job_parser_service.repository;

import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface SkillStatRepository extends JpaRepository<SkillStat, Integer> {

    SkillStat findBySkillNameAndTechAndLevel(String name, String tech, String level);
    List<SkillStat> findByTechAndLevelAndLastUpdate(String tech, String level, LocalDate date);

    @Modifying
    @Query("UPDATE SkillStat AS s SET s.count=?2, s.lastUpdate=?3 WHERE s.id=?1")
    void update(Integer id, Integer count, LocalDate lastUpdate);
}
