package com.emp.gw.task.exception;

import java.io.Serial;

public class NotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -6960286329292643521L;

  public NotFoundException(String message) {
    super(message);
  }
}
