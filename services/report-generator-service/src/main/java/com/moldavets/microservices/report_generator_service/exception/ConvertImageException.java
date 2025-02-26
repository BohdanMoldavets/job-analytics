package com.moldavets.microservices.report_generator_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ConvertImageException extends RuntimeException {
    public ConvertImageException(String message) {
        super(message);
    }
}
