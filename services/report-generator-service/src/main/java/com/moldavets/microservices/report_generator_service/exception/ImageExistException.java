package com.moldavets.microservices.report_generator_service.exception;

public class ImageExistException extends RuntimeException {
    public ImageExistException(String message) {
        super(message);
    }
}
