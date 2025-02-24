package com.moldavets.microservices.report_generator_service.mapper.Impl;

import com.moldavets.microservices.report_generator_service.mapper.SkillMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SkillMapperImpl implements SkillMapper {

    public Map<String,Integer> mapSkills (List<Map<String,String>> skills) {
        Map<String, Integer> mappedSkills = new HashMap<>();

        for (Map<String, String> skill : skills) {
            String tempSkill = "";
            int tempCount = 0;
            for (String key : skill.keySet()) {

                if(key.equals("count")) {
                    tempCount = Integer.parseInt(skill.get(key));
                } else {
                    tempSkill = skill.get(key);
                }
                mappedSkills.put(tempSkill, tempCount);
            }
        }
        return mappedSkills;
    }
}
