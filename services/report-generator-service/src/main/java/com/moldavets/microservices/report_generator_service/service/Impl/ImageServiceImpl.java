package com.moldavets.microservices.report_generator_service.service.Impl;

import com.moldavets.microservices.report_generator_service.entity.ImageEntity;
import com.moldavets.microservices.report_generator_service.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class ImageServiceImpl implements ImageService {

    private RedisTemplate<String, byte[]> redisTemplate;

    @Autowired
    public ImageServiceImpl(RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(ImageEntity imageEntity) {
        if (imageEntity.getId() != null) {
            redisTemplate.opsForValue().set(
                    imageEntity.getId(),imageEntity.getImage(),
                    30,
                    TimeUnit.DAYS
            );
        }
    }

    @Override
    public ImageEntity getImageById(String id) {
        byte[] tempImage = redisTemplate.opsForValue().get(id);
        if (tempImage != null) {
            return new ImageEntity(id, tempImage);
        } else {
            return null;
        }
    }
}
