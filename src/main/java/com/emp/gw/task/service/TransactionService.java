package com.emp.gw.task.service;

import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;

/** Transaction service. This interface is used to define the contract for creating transactions. */
public interface TransactionService {
  TransactionResponseDto createTransaction(TransactionDto transactionRequestDto);
}
