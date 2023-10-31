package com.emp.gw.task.exception;

public class EmpRuntimeException extends RuntimeException {
  public EmpRuntimeException(ReflectiveOperationException e) {
    super(e);
  }
}
