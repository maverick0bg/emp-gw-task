package com.emp.gw.task.service.impl;

import static java.util.Objects.isNull;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;
import com.emp.gw.task.enums.TransactionStatuses;
import com.emp.gw.task.enums.TransactionTypes;
import com.emp.gw.task.exception.InvalidInputException;
import com.emp.gw.task.mapper.TransactionEntityMapper;
import com.emp.gw.task.model.entity.TransactionEntity;
import com.emp.gw.task.repository.TransactionRepository;
import com.emp.gw.task.service.MerchantService;
import com.emp.gw.task.service.TransactionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service(value = TransactionServiceFactory.CHARGE)
@RequiredArgsConstructor
public class ChargeTransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionEntityMapper transactionEntityMapper;
  private final MerchantService merchantService;

  @Override
  @Transactional(value = Transactional.TxType.REQUIRES_NEW)
  public TransactionResponseDto createTransaction(TransactionDto transactionRequestDto) {
    checkAndSetMerchant(transactionRequestDto);
    validate(transactionRequestDto);

    merchantService.addAmountToMerchantBalance(
        transactionRequestDto.getMerchant().getId(), transactionRequestDto.getAmount());

    TransactionEntity saved =
        transactionRepository.save(transactionEntityMapper.toEntity(transactionRequestDto));

    return new TransactionResponseDto(saved.getId(), saved.getTransactionStatus());
  }

  private void validate(TransactionDto transactionRequestDto) {
    if (isNull(transactionRequestDto.getRelatedTransaction())) {
      throw new InvalidInputException("Charge transaction must have a related transaction");
    }
    TransactionEntity relatedTransaction =
        transactionRepository
            .findById(transactionRequestDto.getRelatedTransaction().getId())
            .orElseThrow(() -> new InvalidInputException("Related transaction does not exist"));

    if (!(relatedTransaction.getTransactionStatus().equals(TransactionStatuses.APPROVED)
        || relatedTransaction.getTransactionStatus().equals(TransactionStatuses.REFUNDED))) {
      throw new InvalidInputException("Related transaction has invalid status.");
    }

    if (!relatedTransaction.getTransactionType().equals(TransactionTypes.AUTHORISE)) {
      throw new InvalidInputException("Only authorize transactions can be charged.");
    }

    if (isNull(transactionRequestDto.getAmount())
        || transactionRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidInputException("Amount is required for charge transaction");
    }

    if (!transactionRequestDto.getAmount().equals(relatedTransaction.getAmount())) {
      throw new InvalidInputException(
          "Charge amount can't be different than related transaction amount");
    }
  }

  private void checkAndSetMerchant(TransactionDto transactionRequestDto) {
    MerchantDto merchant =
        merchantService.getActiveMerchant(transactionRequestDto.getMerchant().getId());
    transactionRequestDto.setMerchant(merchant);
  }
}
