package com.moldavets.microservices.report_generator_service.exception.handler;

import com.moldavets.microservices.report_generator_service.exception.ConvertImageException;
import com.moldavets.microservices.report_generator_service.exception.HttpClientNotFoundException;
import com.moldavets.microservices.report_generator_service.exception.IncorrectJsonFormatException;
import com.moldavets.microservices.report_generator_service.exception.ServerMappingException;
import com.moldavets.microservices.report_generator_service.exception.model.ExceptionDetailsModel;
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

    @ExceptionHandler(HttpClientNotFoundException.class)
    public ResponseEntity<ExceptionDetailsModel> handleHttpClientErrorExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectJsonFormatException.class)
    public ResponseEntity<ExceptionDetailsModel> handleIncorrectJsonFormatException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConvertImageException.class)
    public ResponseEntity<ExceptionDetailsModel> handleConvertImageException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(ServerMappingException.class)
    public ResponseEntity<ExceptionDetailsModel> handleServerMappingException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExceptionDetailsModel createExceptionDetailsModel(Exception ex, WebRequest request) {
        return new ExceptionDetailsModel(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}
