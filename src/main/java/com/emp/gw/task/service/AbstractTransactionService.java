package com.emp.gw.task.service;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;
import com.emp.gw.task.mapper.TransactionEntityMapper;
import com.emp.gw.task.model.entity.TransactionEntity;
import com.emp.gw.task.repository.TransactionRepository;
import jakarta.transaction.Transactional;

/**
 * Abstract transaction service. This class defines the common behaviour for manipulating different
 * types of transactions.
 */
public abstract class AbstractTransactionService implements TransactionService {

  @Transactional(value = Transactional.TxType.REQUIRES_NEW)
  public TransactionResponseDto createTransaction(TransactionDto transactionRequestDto) {
    checkAndSetMerchant(transactionRequestDto);
    validateAndSetStatus(transactionRequestDto);

    updateRelatedObjects(transactionRequestDto);

    final TransactionEntity saved = saveTransaction(transactionRequestDto);

    return new TransactionResponseDto(saved.getId(), saved.getTransactionStatus());
  }

  protected abstract void updateRelatedObjects(TransactionDto transactionRequestDto);

  protected TransactionEntity saveTransaction(TransactionDto transactionRequestDto) {
    return getTransactionRepository()
        .save(getTransactionEntityMapper().toEntity(transactionRequestDto));
  }

  protected void checkAndSetMerchant(TransactionDto transactionRequestDto) {
    MerchantDto merchant =
        getMerchantService().getActiveMerchant(transactionRequestDto.getMerchant().getId());
    transactionRequestDto.setMerchant(merchant);
  }

  protected abstract void validateAndSetStatus(TransactionDto transactionRequestDto);

  protected abstract TransactionRepository getTransactionRepository();

  protected abstract TransactionEntityMapper getTransactionEntityMapper();

  protected abstract MerchantService getMerchantService();
}
