package com.moldavets.microservices.job_parser_service.exception.handler;

import com.moldavets.microservices.job_parser_service.exception.LevelNotFoundException;
import com.moldavets.microservices.job_parser_service.exception.LostConnectionException;
import com.moldavets.microservices.job_parser_service.exception.TechNotFoundException;
import com.moldavets.microservices.job_parser_service.exception.model.ExceptionDetailsModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetailsModel> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LostConnectionException.class)
    public ResponseEntity<ExceptionDetailsModel> handleLostConnectionException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(TechNotFoundException.class)
    public ResponseEntity<ExceptionDetailsModel> handleTechNotFoundException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LevelNotFoundException.class)
    public ResponseEntity<ExceptionDetailsModel> handleLevelNotFoundException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.NOT_FOUND);
    }

    private ExceptionDetailsModel createExceptionDetailsModel(Exception ex, WebRequest request) {
        return new ExceptionDetailsModel(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        request.getDescription(false)
                );
    }



}
