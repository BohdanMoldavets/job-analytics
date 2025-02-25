package com.moldavets.microservices.report_generator_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moldavets.microservices.report_generator_service.entity.ImageEntity;
import com.moldavets.microservices.report_generator_service.exception.HttpClientNotFoundException;
import com.moldavets.microservices.report_generator_service.exception.ImageExistException;
import com.moldavets.microservices.report_generator_service.mapper.SkillMapper;
import com.moldavets.microservices.report_generator_service.proxy.JobParserProxy;
import com.moldavets.microservices.report_generator_service.service.ImageGeneratorService;
import com.moldavets.microservices.report_generator_service.service.ImageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report-generator-service")
public class ReportGeneratorController {

    private JobParserProxy jobParserProxy;
    private SkillMapper skillMapper;
    private ImageService imageService;
    private ImageGeneratorService imageGeneratorService;

    private HttpHeaders headers;

    public ReportGeneratorController() {
    }

    @Autowired
    public ReportGeneratorController(ImageService imageService,
                                     ImageGeneratorService imageGeneratorService,
                                     SkillMapper skillMapper,
                                     JobParserProxy jobParserProxy) {
        this.imageService = imageService;
        this.imageGeneratorService = imageGeneratorService;
        this.skillMapper = skillMapper;
        this.jobParserProxy = jobParserProxy;
    }

    @PostConstruct
    public void init() {
        headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");
    }

    @GetMapping("/png/{tech}")
    public ResponseEntity<byte[]> reportGenerator(@PathVariable("tech") String tech,
                                                  @RequestParam("level") String level) {

        String skillsAnalytics;
        List<Map<String,String>> responseSkills;

        ImageEntity storedImageEntity = imageService.getImageById(
                LocalDate.now() + ":" + tech + ":" + level + ":png"
        );

        if (storedImageEntity != null) {
            return new ResponseEntity<>(storedImageEntity.getImage(), headers, HttpStatus.OK);
        }

        try {
            skillsAnalytics = jobParserProxy.retrieveSkillsJson(tech, level);

            responseSkills = new ObjectMapper().readValue(
                    skillsAnalytics,
                    new TypeReference<>(){}
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (HttpClientErrorException.NotFound e) {
            throw new HttpClientNotFoundException(e.getMessage());
        }

        ImageEntity retrievedImageEntity = mapAndSaveImageEntity(responseSkills,tech,level);

        return new ResponseEntity<>(retrievedImageEntity.getImage(), headers, HttpStatus.OK);

    }

    @PostMapping("/png/{tech}")
    public ResponseEntity<byte[]> customReportGenerator(@PathVariable("tech") String tech,
                                                        @RequestParam("level") String level,
                                                        @RequestBody List<Map<String,String>> requestBody) {

        String imagePath = LocalDate.now() + ":" + tech + ":" + level + ":png";

        if (imageService.getImageById(imagePath) != null) {
            throw new ImageExistException("Image [" + imagePath + "] already exist");
        }
        ImageEntity retrievedImageEntity = mapAndSaveImageEntity(requestBody,tech,level);

        return new ResponseEntity<>(retrievedImageEntity.getImage(), headers, HttpStatus.OK);
    }

    private ImageEntity mapAndSaveImageEntity(List<Map<String,String>> jsonBody, String tech, String level) {

        Map<String, Integer> mappedSkills = skillMapper.mapSkills(jsonBody);

        ImageEntity tempEntity =
                new ImageEntity(
                        LocalDate.now() + String.format(":%s:%s:png", tech, level),
                        imageGeneratorService.getImageAsByteArray(mappedSkills, tech)
                );
        imageService.save(tempEntity);
        return tempEntity;
    }
}
