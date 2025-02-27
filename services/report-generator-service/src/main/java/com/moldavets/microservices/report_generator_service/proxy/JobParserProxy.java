package com.moldavets.microservices.report_generator_service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "job-parser-service", url = "localhost:8000")
public interface JobParserProxy {

    @GetMapping("/job-parser-service/{tech}")
    String retrieveSkillsJson(
            @PathVariable String tech,
            @RequestParam String level
    );

}
