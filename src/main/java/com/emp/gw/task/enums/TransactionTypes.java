package com.emp.gw.task.enums;

import com.emp.gw.task.exception.NotFoundException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionTypes {
  AUTHORISE(1),
  CHARGE(2),
  REFUND(3),
  REVERSAL(4);

  private final int type;

  public static TransactionTypes getTransactionType(int type) {
    return Arrays.stream(TransactionTypes.values())
        .filter(t -> t.getType() == type)
        .findFirst()
        .orElseThrow(
            () -> new NotFoundException(String.format("Transaction type (%d) not found", type)));
  }
}
