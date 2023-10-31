package com.emp.gw.task.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ConflictException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<String> handleValidationException(ConflictException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(EmpRuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleEmpRuntimeException(EmpRuntimeException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }
}
