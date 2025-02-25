package com.moldavets.microservices.report_generator_service.service.Impl;

import com.moldavets.microservices.report_generator_service.controller.ReportGeneratorController;
import com.moldavets.microservices.report_generator_service.service.ScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledServiceImpl implements ScheduledService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledServiceImpl.class);

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
//               cron="0 0 4 1 * ?" , initialDelay = 0)
    @Scheduled(timeUnit = TimeUnit.DAYS,
               fixedRate = 2, initialDelay = 0) //todo -> delete this coz it's only for test
    public void scheduledDataCollection() {
        LOGGER.info("Data collection started");
        for (String tempJob : itJobs) {
            for (String tempLevel : itLevels) {
                LOGGER.info("Generation report for " + tempJob + "/" + tempLevel);
                reportGeneratorController.reportGenerator(tempJob, tempLevel);
                LOGGER.info("Generation report for " + tempJob + "/" + tempLevel + " completed");
            }
        }
        LOGGER.info("Data collection ended");
    }


}
