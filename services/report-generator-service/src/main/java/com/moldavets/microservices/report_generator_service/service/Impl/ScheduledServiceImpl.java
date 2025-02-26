package com.moldavets.microservices.report_generator_service.service.Impl;

import com.moldavets.microservices.report_generator_service.controller.ReportGeneratorController;
import com.moldavets.microservices.report_generator_service.service.ScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledServiceImpl implements ScheduledService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledServiceImpl.class);

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
//    @Scheduled(timeUnit = TimeUnit.DAYS,
//               fixedRate = 2, initialDelay = 0) //todo -> delete this coz it's only for test
    public void scheduledDataCollection() {
        logger.info("Data collection started");
        for (String tempJob : itJobs) {
            for (String tempLevel : itLevels) {
                logger.info("Generation report for %s/%s".formatted(tempJob, tempLevel));
                reportGeneratorController.reportGenerator(tempJob, tempLevel);
                logger.info("Generation report for %s/%s completed".formatted(tempJob, tempLevel));
            }
        }
        logger.info("Data collection ended");
    }


}
