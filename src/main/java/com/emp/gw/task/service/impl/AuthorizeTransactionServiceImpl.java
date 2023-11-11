package com.emp.gw.task.service.impl;

import static java.util.Objects.isNull;

import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.exception.InvalidInputException;
import com.emp.gw.task.mapper.TransactionEntityMapper;
import com.emp.gw.task.repository.TransactionRepository;
import com.emp.gw.task.service.AbstractTransactionService;
import com.emp.gw.task.service.MerchantService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service(value = TransactionServiceFactory.AUTHORISE)
@RequiredArgsConstructor
public class AuthorizeTransactionServiceImpl extends AbstractTransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionEntityMapper transactionEntityMapper;
  private final MerchantService merchantService;

  @Override
  protected void updateRelatedObjects(TransactionDto transactionRequestDto) {
    // No related objects to update
  }

  @Override
  protected void validateAndSetStatus(TransactionDto transactionRequestDto) {

    if (!isNull(transactionRequestDto.getRelatedTransaction())) {
      throw new InvalidInputException(
          "Related transaction is not allowed for Authorize transaction");
    }
    if (isNull(transactionRequestDto.getAmount())
        || transactionRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidInputException("Amount is required for Authorize transaction");
    }
  }

  @Override
  protected TransactionRepository getTransactionRepository() {
    return transactionRepository;
  }

  @Override
  protected TransactionEntityMapper getTransactionEntityMapper() {
    return transactionEntityMapper;
  }

  @Override
  protected MerchantService getMerchantService() {
    return merchantService;
  }
}
