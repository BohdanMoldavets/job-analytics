package com.moldavets.microservices.job_parser_service.factory;

import com.moldavets.microservices.job_parser_service.dto.SkillStatDto;
import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SkillStatDtoFactory {

    public List<SkillStatDto> createSkillStatDtoList(List<SkillStat> skillStatList) {
        List<SkillStatDto> skillStatDtoList = new ArrayList<>();

        for (SkillStat skillStat : skillStatList) {
            skillStatDtoList.add(createSkillStatDto(skillStat));
        }
        return skillStatDtoList;
    }

    public SkillStatDto createSkillStatDto(SkillStat skillStat) {
        return new SkillStatDto().builder()
                .skillName(skillStat.getSkillName())
                .count(skillStat.getCount())
                .build();
    }

}
