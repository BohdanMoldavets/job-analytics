package com.moldavets.microservices.job_parser_service.service;

import java.util.Map;

public interface JobScraperService {
    Map<String,Integer> parse(String tech, String level);
}
