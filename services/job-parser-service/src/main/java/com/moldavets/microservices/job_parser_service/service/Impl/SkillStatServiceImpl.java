package com.moldavets.microservices.job_parser_service.service.Impl;

import com.moldavets.microservices.job_parser_service.entity.SkillStat;
import com.moldavets.microservices.job_parser_service.repository.SkillStatRepository;
import com.moldavets.microservices.job_parser_service.service.SkillStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SkillStatServiceImpl implements SkillStatService {

    private SkillStatRepository skillStatRepository;

    @Autowired
    public SkillStatServiceImpl(SkillStatRepository skillStatService) {
        this.skillStatRepository = skillStatService;
    }

    @Override
    public List<SkillStat> getByTechAndLevelAndDate(String tech, String level, LocalDate today) {
        return skillStatRepository.findByTechAndLevelAndLastUpdate(tech, level, today);
    }

    @Override
    @Transactional
    public void saveAll(Map<String, Integer> map, String tech, String level) {
        LocalDate today = LocalDate.now();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            SkillStat tempSkillStat =
                    skillStatRepository.findBySkillNameAndTechAndLevel(entry.getKey(), tech, level);

            if(tempSkillStat != null) {
                skillStatRepository.update(tempSkillStat.getId(), entry.getValue(), today);
                log.info("IN SkillStatServiceImpl.saveAll(): {} {} {} successfully saved", tempSkillStat.getId(), entry.getValue(), today);
            } else {
                SkillStat skillStat = SkillStat.builder()
                        .skillName(entry.getKey())
                        .count(entry.getValue())
                        .lastUpdate(today)
                        .tech(tech)
                        .level(level)
                        .build();
                skillStatRepository.save(skillStat);
                log.info("IN SkillStatServiceImpl.saveAll(): " + skillStat + " successfully saved");
            }
        }
    }
}





