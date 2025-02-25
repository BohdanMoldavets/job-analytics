package com.moldavets.microservices.report_generator_service.exception;

public class HttpClientNotFoundException extends RuntimeException {
  public HttpClientNotFoundException(String message) {
    super(message);
  }
}
