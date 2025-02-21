package com.moldavets.microservices.job_parser_service.controller;

import com.moldavets.microservices.job_parser_service.dto.SkillStatDto;
import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import com.moldavets.microservices.job_parser_service.factory.SkillStatDtoFactory;
import com.moldavets.microservices.job_parser_service.service.JobScraperService;
import com.moldavets.microservices.job_parser_service.service.SkillStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    //job-parser-service/java?level=junior
    @GetMapping("/{tech}")
    public Map<String,Integer> getJob(@PathVariable(value = "tech") String tech,
                                      @RequestParam(value = "level",required = false) String level) {


        Map<String, Integer> resultList;

        //retrieve from db if exist
        Map<String, Integer> retrieveResult ;

        //for new requests
        resultList = jobScraperService.parse(tech, level);

        skillStatService.saveAll(resultList, tech, level);

        return resultList;
    }

    @GetMapping("/test/{tech}")
    public List<SkillStatDto> getJobtest(@PathVariable(value = "tech") String tech,
                                  @RequestParam(value = "level",required = false) String level) {


        List<SkillStat> resultList = skillStatService.getByTechAndLevelAndDate(tech, level, LocalDate.now());

        List<SkillStatDto> responseList = new ArrayList<>();
        for(SkillStat skillStat : resultList) {
            responseList.add(skillStatDtoFactory.createSkillStatDto(skillStat));
        }
        return responseList;
    }

}
