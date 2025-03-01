package com.moldavets.microservices.report_generator_service.service.Impl;

import com.moldavets.microservices.report_generator_service.controller.ReportGeneratorController;
import com.moldavets.microservices.report_generator_service.service.ScheduledService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ScheduledServiceImpl implements ScheduledService {

    @Value("${it.jobs.tech.list}")
    private List<String> itJobs;

    @Value("${it.jobs.level.list}")
    private List<String> itLevels;

    private ReportGeneratorController reportGeneratorController;


    @Autowired
    public ScheduledServiceImpl(ReportGeneratorController reportGeneratorController) {
        this.reportGeneratorController = reportGeneratorController;
    }

    @Override
//    @Scheduled(timeUnit = TimeUnit.MINUTES,
//               cron="0 0 1 * * ?" , initialDelay = 0)
//    @Scheduled(timeUnit = TimeUnit.DAYS,
//               fixedRate = 2, initialDelay = 0) //todo -> delete this coz it's only for test
    public void scheduledDataCollection() {
        log.info("Data collection started");
        for (String tempJob : itJobs) {
            for (String tempLevel : itLevels) {
                log.info("Generation report for %s/%s".formatted(tempJob, tempLevel));
                reportGeneratorController.reportGenerator(tempJob, tempLevel);
                log.info("Generation report for %s/%s completed".formatted(tempJob, tempLevel));
            }
        }
        log.info("Data collection ended");
    }


}
