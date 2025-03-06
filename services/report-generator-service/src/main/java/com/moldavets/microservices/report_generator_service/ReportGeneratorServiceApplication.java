package com.moldavets.microservices.report_generator_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ReportGeneratorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportGeneratorServiceApplication.class, args);
	}

}
