package com.moldavets.microservices.job_parser_service.controller;

import com.moldavets.microservices.job_parser_service.dto.SkillStatDto;
import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import com.moldavets.microservices.job_parser_service.mapper.SkillStatDtoMapper;
import com.moldavets.microservices.job_parser_service.service.JobScraperService;
import com.moldavets.microservices.job_parser_service.service.SkillStatService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
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
    @CircuitBreaker(name = "JobParserController", fallbackMethod = "fallbackJobParserController")
    public List<SkillStatDto> parseSkills(@PathVariable(value = "tech") String tech,
                                          @RequestParam(value = "level") String level) {

        List<SkillStat> skillStatList = skillStatService.getByTechAndLevelAndDate(tech, level, LocalDate.now());

        if(skillStatList == null || skillStatList.isEmpty()) {
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

    private List<SkillStatDto> fallbackJobParserController(String tech, String level, Throwable throwable) {
        log.error("IN JobParserController.fallbackJobParserController() - something went wrong");
        return new ArrayList<>();
    }


}
