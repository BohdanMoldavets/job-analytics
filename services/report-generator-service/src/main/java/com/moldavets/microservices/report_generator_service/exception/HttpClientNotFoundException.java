package com.moldavets.microservices.report_generator_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HttpClientNotFoundException extends RuntimeException {
  public HttpClientNotFoundException(String message) {
    super(message);
  }
}
