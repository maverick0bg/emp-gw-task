package com.emp.gw.task.exception;

/** Exception for invalid input. Used when request data is invalid. */
public class InvalidInputException extends RuntimeException {
  public InvalidInputException(String message) {
    super(message);
  }
}
