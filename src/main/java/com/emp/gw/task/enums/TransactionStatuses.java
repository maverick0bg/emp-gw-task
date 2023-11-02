package com.emp.gw.task.enums;

import com.emp.gw.task.exception.NotFoundException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionStatuses {
  APPROVED(1),
  REVERSED(2),
  REFUNDED(3),
  ERROR(4);

  private final int status;

  public static TransactionStatuses getTransactionStatus(int status) {
    return Arrays.stream(TransactionStatuses.values())
        .filter(t -> t.getStatus() == status)
        .findFirst()
        .orElseThrow(
            () ->
                new NotFoundException(String.format("Transaction status (%d) not found", status)));
  }
}
