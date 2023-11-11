package com.emp.gw.task.service.impl;

import com.emp.gw.task.enums.TransactionTypes;
import com.emp.gw.task.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Transaction service factory. This class is used to get the specific transaction service based on
 * the transaction type.
 */
@Component
@RequiredArgsConstructor
public class TransactionServiceFactory {
  public static final String AUTHORISE = "AUTHORISE";
  static final String CHARGE = "CHARGE";
  static final String REFUND = "REFUND";
  static final String REVERSAL = "REVERSAL";

  private final ApplicationContext applicationContext;

  public TransactionService getTransactionService(TransactionTypes transactionType) {
    return applicationContext.getBean(transactionType.name(), TransactionService.class);
  }
}
