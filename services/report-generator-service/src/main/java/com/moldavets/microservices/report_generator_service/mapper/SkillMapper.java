package com.moldavets.microservices.report_generator_service.mapper;

import java.util.List;
import java.util.Map;

public interface SkillMapper {
    Map<String,Integer> mapSkills (List<Map<String,String>> skills);
}
