package com.moldavets.microservices.job_parser_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobParserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobParserServiceApplication.class, args);
	}

}
