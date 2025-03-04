package com.moldavets.microservices.job_parser_service.controller;

import com.moldavets.microservices.job_parser_service.dto.SkillStatDto;
import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import com.moldavets.microservices.job_parser_service.mapper.SkillStatDtoMapper;
import com.moldavets.microservices.job_parser_service.service.Impl.JobScraperServiceImpl;
import com.moldavets.microservices.job_parser_service.service.Impl.SkillStatServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class JobParserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SkillStatServiceImpl skillStatService;

    @MockitoBean
    JobScraperServiceImpl jobScraperService;

    @MockitoBean
    SkillStatDtoMapper skillStatDtoMapper;

    @DisplayName("parseSkills() should return correct skills list when input contains valid data and value already in db")
    @ParameterizedTest
    @CsvSource(value = {
            "java,junior,5,spring",
            "php,senior,12,laravel",
            "go,mid,42,Golang"
    })
    void parseSkills_shouldReturnCorrectSkillsList_whenInputContainsValidDataAndValueAlreadyInDB(String tech, String level, int count, String skillName) throws Exception {
        SkillStat mockSkillStat = new SkillStat().builder()
                .skillName(skillName)
                .tech(tech)
                .level(level)
                .lastUpdate(LocalDate.now())
                .count(5)
                .build();

        List<SkillStat> mockSkillStatList = List.of(mockSkillStat);

        Mockito.when(skillStatService.getByTechAndLevelAndDate(tech, level, LocalDate.now()))
                .thenReturn(mockSkillStatList);

        Mockito.when(skillStatDtoMapper.createSkillStatDtoList(mockSkillStatList))
                .thenReturn(List.of(new SkillStatDto().builder()
                        .skillName(mockSkillStat.getSkillName())
                        .count(mockSkillStat.getCount())
                        .build())
                );

        mockMvc.perform(MockMvcRequestBuilders.get("/job-parser-service/{tech}",tech)
                .param("level", level))
                .andExpect(jsonPath("$[0].skill_name").value(mockSkillStat.getSkillName()))
                .andExpect(jsonPath("$[0].count").value(mockSkillStat.getCount()));
    }

    @DisplayName("parseSkills() should return correct skills list when input contains valid data but value not in db")
    @ParameterizedTest
    @CsvSource(value = {
            "java,junior,5,hibernate",
            "php,senior,12,symfony",
            "go,mid,42,Beego"
    })
    void parseSkills_shouldReturnCorrectSkillsList_whenInputContainsValidData(String tech, String level, int count, String skillName) throws Exception {
        Mockito.when(skillStatService.getByTechAndLevelAndDate(Mockito.anyString(), Mockito.anyString(), Mockito.any(LocalDate.class)))
                .thenReturn(null)
                .thenReturn(List.of(new SkillStat().builder()
                .skillName(skillName)
                .count(count)
                .build())
        );

        Mockito.when(jobScraperService.parse(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Map.of(skillName,count));

        Mockito.doNothing().when(skillStatService)
                .saveAll(Mockito.anyMap(), Mockito.anyString(), Mockito.anyString());

        Mockito.when(skillStatDtoMapper.createSkillStatDtoList(Mockito.anyList()))
                .thenReturn(List.of(new SkillStatDto().builder()
                        .skillName(skillName)
                        .count(count)
                        .build())
                );

        mockMvc.perform(MockMvcRequestBuilders.get("/job-parser-service/{tech}",tech)
                        .param("level", level))
                        .andExpect(jsonPath("$[0].skill_name").value(skillName))
                        .andExpect(jsonPath("$[0].count").value(count));


        Mockito.verify(jobScraperService, Mockito.times(1)).parse(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(skillStatService, Mockito.times(2)).getByTechAndLevelAndDate(Mockito.anyString(), Mockito.anyString(), Mockito.any(LocalDate.class));
        Mockito.verify(skillStatDtoMapper, Mockito.times(1)).createSkillStatDtoList(Mockito.anyList());

    }


}