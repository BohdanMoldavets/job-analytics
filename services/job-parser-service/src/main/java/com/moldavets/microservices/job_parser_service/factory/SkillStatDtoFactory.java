package com.moldavets.microservices.job_parser_service.factory;

import com.moldavets.microservices.job_parser_service.dto.SkillStatDto;
import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import org.springframework.stereotype.Component;

@Component
public class SkillStatDtoFactory {

    public SkillStatDto createSkillStatDto(SkillStat skillStat) {
        return new SkillStatDto().builder()
                .skillName(skillStat.getSkillName())
                .count(skillStat.getCount())
                .build();
    }

}
