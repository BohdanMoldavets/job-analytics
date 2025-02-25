package com.moldavets.microservices.report_generator_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moldavets.microservices.report_generator_service.entity.ImageEntity;
import com.moldavets.microservices.report_generator_service.mapper.SkillMapper;
import com.moldavets.microservices.report_generator_service.service.ImageGeneratorService;
import com.moldavets.microservices.report_generator_service.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report-generator-service")
public class ReportGeneratorController {

    private SkillMapper skillMapper;
    private ImageService imageService;
    private ImageGeneratorService imageGeneratorService;

    public ReportGeneratorController() {
    }

    @Autowired
    public ReportGeneratorController(ImageService imageService,
                                     ImageGeneratorService imageGeneratorService,
                                     SkillMapper skillMapper) {
        this.imageService = imageService;
        this.imageGeneratorService = imageGeneratorService;
        this.skillMapper = skillMapper;
    }

    @GetMapping("/png/{tech}")
    public ResponseEntity<byte[]> reportGenerator(@PathVariable("tech") String tech,
                                                  @RequestParam("level") String level) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");

        ImageEntity storedImageEntity = imageService.getImageById(
                LocalDate.now() + ":" + tech + ":" + level + ":png"
        );

        if (storedImageEntity != null) {
            return new ResponseEntity<>(storedImageEntity.getImage(), headers, HttpStatus.OK);
        }

        String skillsAnalytics = null;
        List<Map<String,String>> responseSkills;
        try {
            RestTemplate restTemplate = new RestTemplate();

            skillsAnalytics = restTemplate.getForEntity(
                    String.format(
                            "http://localhost:8080/job-parser-service/%s?level=%s",
                            tech,
                            level
                    ), String.class ).getBody();


            responseSkills = new ObjectMapper().readValue(
                    skillsAnalytics,
                    new TypeReference<>(){}
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (HttpClientErrorException.NotFound e) {
            //todo all exceptions
            return null;
        }

            Map<String, Integer> mappedSkills = skillMapper.mapSkills(responseSkills);

            ImageEntity tempEntity =
                    new ImageEntity(
                            LocalDate.now() + String.format(":%s:%s:png", tech, level),
                            imageGeneratorService.getImageAsByteArray(mappedSkills, tech)
                    );
            imageService.save(tempEntity);

            return new ResponseEntity<>(tempEntity.getImage(), headers, HttpStatus.OK);

    }

}
