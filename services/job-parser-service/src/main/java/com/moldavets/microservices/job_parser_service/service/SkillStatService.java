package com.moldavets.microservices.job_parser_service.service;


import com.moldavets.microservices.job_parser_service.entity.SkillStat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SkillStatService {

    List<SkillStat> getByTechAndLevelAndDate(String tech, String level, LocalDate today);

    void saveAll(Map<String, Integer> map, String tech, String level);


}
