package com.moldavets.microservices.report_generator_service.service;

import java.util.Map;

public interface ImageGeneratorService {
    byte[] getImageAsByteArray(Map<String, Integer> skills, String tech);
}
