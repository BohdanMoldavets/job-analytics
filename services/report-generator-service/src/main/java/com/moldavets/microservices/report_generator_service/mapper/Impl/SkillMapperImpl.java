package com.moldavets.microservices.report_generator_service.mapper.Impl;

import com.moldavets.microservices.report_generator_service.exception.IncorrectJsonFormatException;
import com.moldavets.microservices.report_generator_service.mapper.SkillMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SkillMapperImpl implements SkillMapper {

    public Map<String,Integer> mapSkills (List<Map<String,String>> skills) {
        Map<String, Integer> mappedSkills = new HashMap<>();

        for (Map<String, String> skill : skills) {
            String tempSkill = "";
            int tempCount = 0;

            for (Map.Entry<String, String> entry : skill.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if(key.equals("count")) {
                    tempCount = Integer.parseInt(value);
                } else if (key.equals("skill_name")) {
                    tempSkill = value;
                } else {
                    throw new IncorrectJsonFormatException("Incorrect Json Format");
                }
                mappedSkills.put(tempSkill, tempCount);
            }

        }
        log.info("IN SkillMapperImpl.mapSkills(): result - {}", mappedSkills);
        return mappedSkills;
    }
}
