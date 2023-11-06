package com.emp.gw.task.service;

/** Clean up transactions older than predefined value. */
public interface TransactionsCleanupService {
  void cleanUpTransactions();
}
