package com.emp.gw.task.service.impl;

import static java.util.Objects.isNull;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;
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

@Service(value = TransactionServiceFactory.AUTHORISE)
@RequiredArgsConstructor
public class AuthorizeTransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionEntityMapper transactionEntityMapper;
  private final MerchantService merchantService;

  @Override
  @Transactional(value = Transactional.TxType.REQUIRES_NEW)
  public TransactionResponseDto createTransaction(TransactionDto transactionRequestDto) {
    MerchantDto merchant =
        merchantService.getActiveMerchant(transactionRequestDto.getMerchant().getId());
    transactionRequestDto.setMerchant(merchant);
    if (!isNull(transactionRequestDto.getRelatedTransaction())) {
      throw new InvalidInputException(
          "Related transaction is not allowed for Authorize transaction");
    }
    if (isNull(transactionRequestDto.getAmount())
        || transactionRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidInputException("Amount is required for Authorize transaction");
    }
    TransactionEntity saved =
        transactionRepository.save(transactionEntityMapper.toEntity(transactionRequestDto));

    return new TransactionResponseDto(saved.getId(), saved.getTransactionStatus());
  }
}
