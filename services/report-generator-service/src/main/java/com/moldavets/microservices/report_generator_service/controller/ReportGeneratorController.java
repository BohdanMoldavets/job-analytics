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
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    public ReportGeneratorController(ImageService imageService,
                                     ImageGeneratorService imageGeneratorService,
                                     SkillMapper skillMapper) {
        this.imageService = imageService;
        this.imageGeneratorService = imageGeneratorService;
        this.skillMapper = skillMapper;
    }

    @GetMapping("/{tech}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("tech") String tech,
                                                    @RequestParam("level") String level) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");

        ImageEntity storedImageEntity = imageService.getImageById(
                LocalDate.now() + ":" + tech + ":" + level + ":png"
        );
        return new ResponseEntity<>(storedImageEntity.getImage(), headers, HttpStatus.OK);
    }


    @GetMapping("/png/{tech}")
    public ResponseEntity<?> reportGenerator(@PathVariable("tech") String tech,
                                             @RequestParam("level") String level) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String skillsAnalytics = restTemplate.getForEntity(
                    String.format(
                            "http://localhost:8080/job-parser-service/%s?level=%s",
                            tech,
                            level
                    ), String.class ).getBody();

            List<Map<String,String>> responseSkills = new ObjectMapper().readValue(
                    skillsAnalytics,
                    new TypeReference<>(){}
            );

            Map<String, Integer> mappedSkills = skillMapper.mapSkills(responseSkills);

            ImageEntity tempEntity =
                    new ImageEntity(
                            LocalDate.now() + String.format(":%s:%s:png", tech, level),
                            imageGeneratorService.getImageAsByteArray(mappedSkills)
                    );
            imageService.save(tempEntity);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //todo
        return null;
    }

}
