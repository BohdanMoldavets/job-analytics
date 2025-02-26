package com.moldavets.microservices.report_generator_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectJsonFormatException extends RuntimeException {
    public IncorrectJsonFormatException(String message) {
        super(message);
    }
}
