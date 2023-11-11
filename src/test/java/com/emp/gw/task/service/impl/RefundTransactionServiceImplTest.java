package com.emp.gw.task.service.impl;

import static com.emp.gw.task.enums.TransactionStatuses.APPROVED;
import static com.emp.gw.task.enums.TransactionTypes.AUTHORISE;
import static com.emp.gw.task.enums.TransactionTypes.CHARGE;
import static com.emp.gw.task.enums.TransactionTypes.REFUND;
import static com.emp.gw.task.enums.TransactionTypes.REVERSAL;
import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;
import com.emp.gw.task.exception.InvalidInputException;
import com.emp.gw.task.mapper.TransactionEntityMapper;
import com.emp.gw.task.model.entity.TransactionEntity;
import com.emp.gw.task.repository.TransactionRepository;
import com.emp.gw.task.service.MerchantService;
import com.emp.gw.task.service.TransactionService;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RefundTransactionServiceImplTest {
  @Mock private TransactionRepository transactionRepository;
  @Mock private MerchantService merchantService;
  TransactionService transactionService;
  private TransactionEntityMapper mapper;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mapper = Mappers.getMapper(TransactionEntityMapper.class);
    transactionService =
        new RefundTransactionServiceImpl(transactionRepository, mapper, merchantService);
  }

  @Test
  void createTransaction_willSuccess() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    final BigDecimal amount = BigDecimal.valueOf(0.03);
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .amount(amount)
            .build();

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(REVERSAL)
            .relatedTransaction(relatedTransaction)
            .build();
    when(transactionRepository.findById(Mockito.any(UUID.class)))
        .thenReturn(java.util.Optional.of(relatedTransaction));
    when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(transaction);
    TransactionResponseDto transactionResult =
        transactionService.createTransaction(
            TransactionDto.builder()
                .transactionType(REFUND)
                .transactionStatus(APPROVED)
                .merchant(merchantDto)
                .amount(amount)
                .relatedTransaction(mapper.toDto(relatedTransaction))
                .build());
    assertThat(transactionResult.getTransactionStatus(), is(APPROVED));
    assertThat(transactionResult.getId(), is(notNullValue()));
    // save transaction will be invoked twice, once for the reversal transaction and once for the
    // related transaction
    verify(transactionRepository, Mockito.times(2)).save(Mockito.any(TransactionEntity.class));
  }

  @Test
  void createTransaction_willFail_forInvalidRelatedTransactionType() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final BigDecimal amount = BigDecimal.valueOf(0.02);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(AUTHORISE)
            .amount(amount)
            .build();

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(REFUND)
            .relatedTransaction(relatedTransaction)
            .amount(amount)
            .build();
    when(transactionRepository.findById(Mockito.any(UUID.class)))
        .thenReturn(java.util.Optional.of(relatedTransaction));
    when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(transaction);

    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(amount)
            .relatedTransaction(mapper.toDto(relatedTransaction))
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(exception.getMessage(), is("Only charge transactions can be refunded."));
  }

  @Test
  void createTransaction_willFail_forInvalidRelatedTransactionStatus() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final BigDecimal amount = BigDecimal.valueOf(0.02);

    when(transactionRepository.findById(Mockito.any(UUID.class))).thenReturn(empty());

    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(amount)
            .relatedTransaction(TransactionDto.builder().id(UUID.randomUUID()).build())
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(exception.getMessage(), is("Related transaction does not exist"));
  }

  @Test
  void createTransaction_willFail_forInvalidRelatedTransactionAmount() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final BigDecimal amount = BigDecimal.valueOf(0.02);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .amount(amount)
            .build();

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(REFUND)
            .relatedTransaction(relatedTransaction)
            .amount(amount)
            .build();
    when(transactionRepository.findById(Mockito.any(UUID.class)))
        .thenReturn(java.util.Optional.of(relatedTransaction));
    when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(transaction);

    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(BigDecimal.valueOf(0.03))
            .relatedTransaction(mapper.toDto(relatedTransaction))
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(
        exception.getMessage(),
        is("Refund amount can't be different than related transaction amount"));
  }

  @Test
  void createTransaction_willFail_forEmptyRelatedTransaction() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(BigDecimal.valueOf(0.03))
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(
        exception.getMessage(),
        is("Refund transaction must have a related transaction"));
  }

  @Test
  void createTransaction_willFail_forMissingAmount() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .relatedTransaction(TransactionDto.builder().id(UUID.randomUUID()).build())
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(
        exception.getMessage(),
        is("Amount is required for refund transaction"));
  }
}
