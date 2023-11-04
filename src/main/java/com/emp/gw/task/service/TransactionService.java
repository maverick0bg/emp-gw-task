package com.emp.gw.task.service;

import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;

public interface TransactionService {
  TransactionResponseDto createTransaction(TransactionDto transactionRequestDto);
}
