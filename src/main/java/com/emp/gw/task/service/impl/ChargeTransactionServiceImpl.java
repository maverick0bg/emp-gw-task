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

/**
 * Charge transaction service. Implements the specific logic for Charge transactions.
 *
 * <p>Charge transaction - has amount and used to charge customer's amount. Charge Transaction must
 * refer Authorize Transaction.
 *
 * <p>Charge Transaction can be referenced from Reversal Transaction. Only approved or refunded
 * transactions can be referenced, otherwise the status of the submitted transaction will be created
 * with error status.</p>
 */
@Service(value = TransactionServiceFactory.CHARGE)
@RequiredArgsConstructor
public class ChargeTransactionServiceImpl extends AbstractTransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionEntityMapper transactionEntityMapper;
  private final MerchantService merchantService;

  protected void validateAndSetStatus(TransactionDto transactionRequestDto) {
    if (isNull(transactionRequestDto.getRelatedTransaction())) {
      throw new InvalidInputException("Charge transaction must have a related transaction");
    }

    if (isNull(transactionRequestDto.getAmount())
        || transactionRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidInputException("Amount is required for charge transaction");
    }

    TransactionEntity relatedTransaction =
        transactionRepository
            .findById(transactionRequestDto.getRelatedTransaction().getId())
            .orElseThrow(() -> new InvalidInputException("Related transaction does not exist"));

    if (!transactionRequestDto.getAmount().equals(relatedTransaction.getAmount())) {
      throw new InvalidInputException(
          "Charge amount can't be different than related transaction amount");
    }

    if (!relatedTransaction.getTransactionType().equals(TransactionTypes.AUTHORISE)) {
      throw new InvalidInputException("Only authorize transactions can be charged.");
    }

    if (!(relatedTransaction.getTransactionStatus().equals(TransactionStatuses.APPROVED)
        || relatedTransaction.getTransactionStatus().equals(TransactionStatuses.REFUNDED))) {
      transactionRequestDto.setTransactionStatus(TransactionStatuses.ERROR);
    }
  }

  @Override
  protected void updateRelatedObjects(TransactionDto transactionRequestDto) {
    merchantService.addAmountToMerchantBalance(
        transactionRequestDto.getMerchant().getId(), transactionRequestDto.getAmount());
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
