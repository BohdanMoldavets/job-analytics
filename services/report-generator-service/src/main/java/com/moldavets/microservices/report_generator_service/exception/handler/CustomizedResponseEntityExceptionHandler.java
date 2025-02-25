package com.moldavets.microservices.report_generator_service.exception.handler;

import com.moldavets.microservices.report_generator_service.exception.HttpClientNotFoundException;
import com.moldavets.microservices.report_generator_service.exception.model.ExceptionDetailsModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetailsModel> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionDetailsModel exceptionDetails =
                new ExceptionDetailsModel(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        request.getDescription(false)
                );
        return new ResponseEntity<>(exceptionDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientNotFoundException.class)
    public ResponseEntity<ExceptionDetailsModel> handleHttpClientErrorExceptions(Exception ex, WebRequest request) {
        ExceptionDetailsModel exceptionDetails =
                new ExceptionDetailsModel(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        request.getDescription(false)
                );
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }



}
