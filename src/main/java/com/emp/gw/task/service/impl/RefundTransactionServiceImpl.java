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
 * Refund transaction service. Implements the specific logic for Refund transactions.
 *
 * <p>Refund transaction - has amount and used to refund customer's amount. Refund Transaction must
 * refer Charge Transaction. Refund transaction subtracts amount from merchant's balance.
 */
@Service(value = TransactionServiceFactory.REFUND)
@RequiredArgsConstructor
public class RefundTransactionServiceImpl extends AbstractTransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionEntityMapper transactionEntityMapper;
  private final MerchantService merchantService;
  private TransactionEntity relatedTransaction;

  @Override
  protected void validateAndSetStatus(TransactionDto transactionRequestDto) {
    if (isNull(transactionRequestDto.getRelatedTransaction())) {
      throw new InvalidInputException("Refund transaction must have a related transaction");
    }

    if (isNull(transactionRequestDto.getAmount())
        || transactionRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidInputException("Amount is required for refund transaction");
    }

    relatedTransaction =
        transactionRepository
            .findById(transactionRequestDto.getRelatedTransaction().getId())
            .orElseThrow(() -> new InvalidInputException("Related transaction does not exist"));

    if (!transactionRequestDto.getAmount().equals(relatedTransaction.getAmount())) {
      throw new InvalidInputException(
          "Refund amount can't be different than related transaction amount");
    }

    if (!relatedTransaction.getTransactionType().equals(TransactionTypes.CHARGE)) {
      throw new InvalidInputException("Only charge transactions can be refunded.");
    }

    if (!relatedTransaction.getTransactionStatus().equals(TransactionStatuses.APPROVED)) {
      transactionRequestDto.setTransactionStatus(TransactionStatuses.ERROR);
    }
  }

  @Override
  protected void updateRelatedObjects(TransactionDto transactionRequestDto) {
    merchantService.subtractAmountFromMerchantBalance(
        transactionRequestDto.getMerchant().getId(), transactionRequestDto.getAmount());
    relatedTransaction.setTransactionStatus(TransactionStatuses.REFUNDED);
    transactionRepository.save(relatedTransaction);
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
