package com.moldavets.microservices.report_generator_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ImageExistException extends RuntimeException {
    public ImageExistException(String message) {
        super(message);
    }
}
