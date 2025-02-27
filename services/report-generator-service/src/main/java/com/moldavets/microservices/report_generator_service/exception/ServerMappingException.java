package com.moldavets.microservices.report_generator_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerMappingException extends RuntimeException {
    public ServerMappingException(String message) {
        super(message);
    }
}
