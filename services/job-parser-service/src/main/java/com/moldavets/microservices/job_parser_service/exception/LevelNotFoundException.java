package com.moldavets.microservices.job_parser_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LevelNotFoundException extends IllegalArgumentException{

    public LevelNotFoundException(String message) {
        super(message);
    }
}
