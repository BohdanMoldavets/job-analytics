package com.moldavets.microservices.report_generator_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReportGeneratorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportGeneratorServiceApplication.class, args);
	}

}
