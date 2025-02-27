package com.moldavets.microservices.report_generator_service.service;

import com.moldavets.microservices.report_generator_service.entity.ImageEntity;

public interface ImageService {
    void save(ImageEntity imageEntity);
    ImageEntity getImageById(String id);
}
