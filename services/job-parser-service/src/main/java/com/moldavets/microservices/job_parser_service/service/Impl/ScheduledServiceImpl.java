package com.moldavets.microservices.job_parser_service.service.Impl;

import com.moldavets.microservices.job_parser_service.controller.JobParserController;
import com.moldavets.microservices.job_parser_service.service.ScheduledService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ScheduledServiceImpl implements ScheduledService {

    @Value("${it.jobs.tech.list}")
    private List<String> itJobs;

    @Value("${it.jobs.level.list}")
    private List<String> itLevels;

    private JobParserController jobParserController;


    @Autowired
    public ScheduledServiceImpl(JobParserController jobParserController) {
        this.jobParserController = jobParserController;
    }

    @Override
    @Scheduled(timeUnit = TimeUnit.MINUTES,
               cron="0 0 1 * * ?" , initialDelay = 0)
    public void scheduledDataCollection() {
        log.info("Data collection started");
        for (String tempJob : itJobs) {
            for (String tempLevel : itLevels) {
                log.info("Generation report for %s/%s".formatted(tempJob, tempLevel));
                jobParserController.parseSkills(tempJob, tempLevel);
                log.info("Generation report for %s/%s completed".formatted(tempJob, tempLevel));
            }
        }
        log.info("Data collection ended");
    }


}
