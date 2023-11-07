package com.emp.gw.task.service.impl;

import static com.emp.gw.task.enums.TransactionStatuses.APPROVED;
import static com.emp.gw.task.enums.TransactionTypes.AUTHORISE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;
import com.emp.gw.task.exception.InvalidInputException;
import com.emp.gw.task.exception.NotFoundException;
import com.emp.gw.task.mapper.TransactionEntityMapper;
import com.emp.gw.task.model.entity.TransactionEntity;
import com.emp.gw.task.repository.TransactionRepository;
import com.emp.gw.task.service.MerchantService;
import com.emp.gw.task.service.TransactionService;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(Lifecycle.PER_CLASS)
class AuthorizeTransactionServiceImplTest {

  @Mock private TransactionRepository transactionRepository;
  @Mock private MerchantService merchantService;

  TransactionService transactionService;
  private TransactionEntityMapper mapper;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mapper = Mappers.getMapper(TransactionEntityMapper.class);
    transactionService =
        new AuthorizeTransactionServiceImpl(transactionRepository, mapper, merchantService);
  }

  @Test
  void createTransaction_willSuccess() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    when(transactionRepository.save(Mockito.any(TransactionEntity.class)))
        .thenReturn(
            TransactionEntity.builder()
                .id(UUID.randomUUID())
                .transactionStatus(APPROVED)
                .transactionType(AUTHORISE)
                .build());
    TransactionResponseDto transactionResult =
        transactionService.createTransaction(
            TransactionDto.builder()
                .transactionType(AUTHORISE)
                .transactionStatus(APPROVED)
                .merchant(merchantDto)
                .amount(BigDecimal.valueOf(0.02))
                .build());
    assertThat(transactionResult.getTransactionStatus(), is(APPROVED));
    assertThat(transactionResult.getId(), is(notNullValue()));
    verify(transactionRepository, Mockito.times(1)).save(Mockito.any(TransactionEntity.class));
  }

  @Test
  void createTransaction_willFailWithNotFound() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenThrow(new NotFoundException(""));
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(AUTHORISE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .build();
    Assert.assertThrows(
        NotFoundException.class,
        () -> transactionService.createTransaction(transactionDto));
  }

  @Test
  void createTransaction_willFailWithInvalidInputException() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    when(transactionRepository.save(Mockito.any(TransactionEntity.class)))
        .thenReturn(
            TransactionEntity.builder()
                .id(UUID.randomUUID())
                .transactionStatus(APPROVED)
                .transactionType(AUTHORISE)
                .build());
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(AUTHORISE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(BigDecimal.valueOf(0.02))
            .relatedTransaction(TransactionDto.builder().id(UUID.randomUUID()).build())
            .build();
    Assert.assertThrows(
        InvalidInputException.class,
        () -> transactionService.createTransaction(transactionDto));
    transactionDto.setAmount(BigDecimal.ZERO);
    transactionDto.setRelatedTransaction(null);
    Assert.assertThrows(
        InvalidInputException.class,
        () -> transactionService.createTransaction(transactionDto));
  }
}
