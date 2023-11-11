package com.emp.gw.task.service.impl;

import static java.util.Objects.isNull;

import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.enums.TransactionStatuses;
import com.emp.gw.task.enums.TransactionTypes;
import com.emp.gw.task.exception.InvalidInputException;
import com.emp.gw.task.mapper.TransactionEntityMapper;
import com.emp.gw.task.model.entity.TransactionEntity;
import com.emp.gw.task.repository.TransactionRepository;
import com.emp.gw.task.service.AbstractTransactionService;
import com.emp.gw.task.service.MerchantService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service(value = TransactionServiceFactory.REVERSAL)
@RequiredArgsConstructor
public class ReversalTransactionServiceImpl extends AbstractTransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionEntityMapper transactionEntityMapper;
  private final MerchantService merchantService;
  private TransactionEntity relatedTransaction;

  protected void validateAndSetStatus(TransactionDto transactionRequestDto) {
    if (isNull(transactionRequestDto.getRelatedTransaction())) {
      throw new InvalidInputException("Reversal transaction must have a related transaction");
    }
    relatedTransaction =
        transactionRepository
            .findById(transactionRequestDto.getRelatedTransaction().getId())
            .orElseThrow(() -> new InvalidInputException("Related transaction does not exist"));

    if (!relatedTransaction.getTransactionType().equals(TransactionTypes.AUTHORISE)) {
      throw new InvalidInputException("Only authorize transactions can be reversed.");
    }

    if (!(relatedTransaction.getTransactionStatus().equals(TransactionStatuses.APPROVED))) {
      throw new InvalidInputException("Related transaction has invalid status.");
    }

    if (!(isNull(transactionRequestDto.getAmount())
        || transactionRequestDto.getAmount().compareTo(BigDecimal.ZERO) == 0)) {
      throw new InvalidInputException("Amount is not allowed for reversal transaction");
    }
  }

  @Override
  protected void updateRelatedObjects(TransactionDto transactionRequestDto) {
    relatedTransaction.setTransactionStatus(TransactionStatuses.REVERSED);
    transactionRepository.save(relatedTransaction);
  }

  @Override
  protected TransactionRepository getTransactionRepository() {
    return this.transactionRepository;
  }

  @Override
  protected TransactionEntityMapper getTransactionEntityMapper() {
    return this.transactionEntityMapper;
  }

  @Override
  protected MerchantService getMerchantService() {
    return this.merchantService;
  }
}
