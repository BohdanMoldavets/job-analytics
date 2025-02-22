package com.moldavets.microservices.job_parser_service.exception.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionDetailsModel {
    LocalDateTime timestamp;
    String message;
    String details;
}
