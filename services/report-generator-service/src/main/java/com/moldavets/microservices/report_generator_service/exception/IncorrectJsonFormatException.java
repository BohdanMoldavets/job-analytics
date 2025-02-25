package com.moldavets.microservices.report_generator_service.exception;

public class IncorrectJsonFormatException extends RuntimeException {
    public IncorrectJsonFormatException(String message) {
        super(message);
    }
}
