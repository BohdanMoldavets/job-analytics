package com.moldavets.microservices.job_parser_service.controller;

import com.moldavets.microservices.job_parser_service.dto.SkillStatDto;
import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import com.moldavets.microservices.job_parser_service.mapper.SkillStatDtoMapper;
import com.moldavets.microservices.job_parser_service.service.JobScraperService;
import com.moldavets.microservices.job_parser_service.service.SkillStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/job-parser-service")
public class JobParserController {

    private JobScraperService jobScraperService;
    private SkillStatService skillStatService;
    private SkillStatDtoMapper skillStatDtoMapper;

    @Autowired
    public JobParserController(JobScraperService jobScraperService,
                               SkillStatService skillStatService,
                               SkillStatDtoMapper skillStatDtoMapper) {
        this.jobScraperService = jobScraperService;
        this.skillStatService = skillStatService;
        this.skillStatDtoMapper = skillStatDtoMapper;
    }

    @GetMapping("/{tech}")
    public List<SkillStatDto> parseSkills(@PathVariable(value = "tech") String tech,
                                          @RequestParam(value = "level") String level) {

        List<SkillStat> skillStatList = skillStatService.getByTechAndLevelAndDate(tech, level, LocalDate.now());

        if(skillStatList.isEmpty()) {
            Map<String, Integer> rateMap = jobScraperService.parse(tech, level);

            skillStatService.saveAll(
                    rateMap,
                    tech,
                    level
            );
            skillStatList = skillStatService.getByTechAndLevelAndDate(tech, level, LocalDate.now());
        }
        return skillStatDtoMapper.createSkillStatDtoList(skillStatList);
    }

}
