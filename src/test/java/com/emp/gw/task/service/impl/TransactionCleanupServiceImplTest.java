package com.emp.gw.task.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.emp.gw.task.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(Lifecycle.PER_CLASS)
class TransactionCleanupServiceImplTest {

  @Mock private TransactionRepository transactionRepository;
  private TransactionCleanupServiceImpl transactionCleanupService;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.initMocks(this);
    transactionCleanupService = new TransactionCleanupServiceImpl(transactionRepository);
  }

  @Test
  void cleanUpTransactions() {
    when(transactionRepository.deleteByCreatedDateBefore(Mockito.any())).thenReturn(1);
    transactionCleanupService.cleanUpTransactions();
    verify(transactionRepository, times(1)).deleteByCreatedDateBefore(Mockito.any());
  }
}
