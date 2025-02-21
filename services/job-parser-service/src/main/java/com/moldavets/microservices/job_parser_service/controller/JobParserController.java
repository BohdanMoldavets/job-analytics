package com.moldavets.microservices.job_parser_service.controller;

import com.moldavets.microservices.job_parser_service.dto.SkillStatDto;
import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import com.moldavets.microservices.job_parser_service.factory.SkillStatDtoFactory;
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
    private SkillStatDtoFactory skillStatDtoFactory;

    @Autowired
    public JobParserController(JobScraperService jobScraperService,
                               SkillStatService skillStatService,
                               SkillStatDtoFactory skillStatDtoFactory) {
        this.jobScraperService = jobScraperService;
        this.skillStatService = skillStatService;
        this.skillStatDtoFactory = skillStatDtoFactory;
    }

    @GetMapping("/{tech}")
    public List<SkillStatDto> getJob(@PathVariable(value = "tech") String tech,
                                      @RequestParam(value = "level",required = false) String level) {

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
        return skillStatDtoFactory.createSkillStatDtoList(skillStatList);

    }

}
