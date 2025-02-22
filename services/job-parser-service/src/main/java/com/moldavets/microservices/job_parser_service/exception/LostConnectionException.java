package com.moldavets.microservices.job_parser_service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class LostConnectionException extends RuntimeException {

    public LostConnectionException(String message) {
        super(message);
    }
}
