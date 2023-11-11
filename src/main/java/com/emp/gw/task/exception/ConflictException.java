package com.emp.gw.task.exception;

import java.io.Serial;

/** Exception for conflict. Used when request data conflicts with the data inside the database. */
public class ConflictException extends RuntimeException {
  @Serial private static final long serialVersionUID = -6960286429292643521L;

  public ConflictException(String message) {
    super(message);
  }
}
