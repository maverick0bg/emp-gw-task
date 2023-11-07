package com.emp.gw.task.controller;

import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;
import com.emp.gw.task.service.impl.TransactionServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/transaction",
    produces = "application/json",
    consumes = "application/json")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionServiceFactory transactionServiceFactory;

  @PostMapping
  public TransactionResponseDto createTransaction(
      @RequestBody @Validated TransactionDto transactionRequestDto) {
    return transactionServiceFactory
        .getTransactionService(transactionRequestDto.getTransactionType())
        .createTransaction(transactionRequestDto);
  }
}