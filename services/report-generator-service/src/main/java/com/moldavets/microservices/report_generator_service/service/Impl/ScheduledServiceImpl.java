package com.moldavets.microservices.report_generator_service.service.Impl;

import com.moldavets.microservices.report_generator_service.controller.ReportGeneratorController;
import com.moldavets.microservices.report_generator_service.service.ScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledServiceImpl implements ScheduledService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledServiceImpl.class);

    private ReportGeneratorController reportGeneratorController;


    @Autowired
    public ScheduledServiceImpl(ReportGeneratorController reportGeneratorController) {
        this.reportGeneratorController = reportGeneratorController;
    }

    @Override
//    @Scheduled(timeUnit = TimeUnit.MINUTES,
//               cron="0 0 4 1 * ?" , initialDelay = 0)
    @Scheduled(timeUnit = TimeUnit.MINUTES,
               fixedRate = 2, initialDelay = 0) //todo -> delete this coz it's only for test
    public void scheduledDataCollection() {

        List<String> list = new ArrayList<>();
        list.add("ux");
        list.add("java");
        list.add("php");

        for (String s : list) {
            LOGGER.info("Started generation report for " + s);
            reportGeneratorController.reportGenerator(s, "junior");
            LOGGER.info("Ended generation report for " + s);
        }
    }


}
