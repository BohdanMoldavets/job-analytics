package com.moldavets.microservices.report_generator_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@RedisHash("Image")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ImageEntity implements Serializable {
    String id;
    byte[] image;
}

