package com.emp.gw.task.service.impl;

import static com.emp.gw.task.enums.TransactionStatuses.APPROVED;
import static com.emp.gw.task.enums.TransactionStatuses.REVERSED;
import static com.emp.gw.task.enums.TransactionTypes.AUTHORISE;
import static com.emp.gw.task.enums.TransactionTypes.CHARGE;
import static com.emp.gw.task.enums.TransactionTypes.REVERSAL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.dto.TransactionResponseDto;
import com.emp.gw.task.enums.TransactionStatuses;
import com.emp.gw.task.exception.InvalidInputException;
import com.emp.gw.task.mapper.TransactionEntityMapper;
import com.emp.gw.task.model.entity.TransactionEntity;
import com.emp.gw.task.repository.TransactionRepository;
import com.emp.gw.task.service.MerchantService;
import com.emp.gw.task.service.TransactionService;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChargeTransactionServiceImplTest {

  @Mock private TransactionRepository transactionRepository;
  @Mock private MerchantService merchantService;
  @Captor private ArgumentCaptor<TransactionEntity> transactionEntityArgumentCaptor;

  TransactionService transactionService;
  private TransactionEntityMapper mapper;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mapper = Mappers.getMapper(TransactionEntityMapper.class);
    transactionService =
        new ChargeTransactionServiceImpl(transactionRepository, mapper, merchantService);
  }

  @Test
  void createTransaction_willSuccess() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(AUTHORISE)
            .amount(BigDecimal.valueOf(0.02))
            .build();

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .relatedTransaction(relatedTransaction)
            .build();
    when(transactionRepository.findById(Mockito.any(UUID.class)))
        .thenReturn(java.util.Optional.of(relatedTransaction));
    when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(transaction);
    TransactionResponseDto transactionResult =
        transactionService.createTransaction(
            TransactionDto.builder()
                .transactionType(CHARGE)
                .transactionStatus(APPROVED)
                .merchant(merchantDto)
                .amount(BigDecimal.valueOf(0.02))
                .relatedTransaction(mapper.toDto(relatedTransaction))
                .build());
    assertThat(transactionResult.getTransactionStatus(), is(APPROVED));
    assertThat(transactionResult.getId(), is(notNullValue()));
    verify(transactionRepository, Mockito.times(1)).save(Mockito.any(TransactionEntity.class));
  }

  @Test
  @DisplayName("Create transaction will fail for invalid amount compared to related transaction")
  void createTransaction_will_fail_for_invalidAmount() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(AUTHORISE)
            .amount(BigDecimal.valueOf(0.02))
            .build();

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .relatedTransaction(relatedTransaction)
            .amount(BigDecimal.valueOf(0.01))
            .build();
    when(transactionRepository.findById(Mockito.any(UUID.class)))
        .thenReturn(java.util.Optional.of(relatedTransaction));
    when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(transaction);
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(BigDecimal.valueOf(0.01))
            .relatedTransaction(mapper.toDto(relatedTransaction))
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(
        exception.getMessage(),
        is("Charge amount can't be different than related transaction amount"));
  }

  @Test
  @DisplayName("Create transaction will fail for missing amount")
  void createTransaction_will_fail_for_missingAmount() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(AUTHORISE)
            .amount(BigDecimal.valueOf(0.02))
            .build();

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .relatedTransaction(relatedTransaction)
            .amount(BigDecimal.valueOf(0.01))
            .build();
    when(transactionRepository.findById(Mockito.any(UUID.class)))
        .thenReturn(java.util.Optional.of(relatedTransaction));
    when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(transaction);
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .relatedTransaction(mapper.toDto(relatedTransaction))
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(exception.getMessage(), is("Amount is required for charge transaction"));
  }

  @Test
  @DisplayName("Create transaction will fail for missing related transaction in the database")
  void createTransaction_willFail_for_invalidRelatedTransaction() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(AUTHORISE)
            .build();

    when(transactionRepository.findById(relatedTransaction.getId()))
        .thenReturn(java.util.Optional.empty());

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .relatedTransaction(relatedTransaction)
            .build();

    when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(transaction);
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(BigDecimal.valueOf(0.02))
            .relatedTransaction(mapper.toDto(relatedTransaction))
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(exception.getMessage(), is("Related transaction does not exist"));
  }

  @Test
  @DisplayName(
      "Create transaction will store with status ERROR for invalid related transaction status")
  void createTransaction_willFail_for_invalidRelatedTransactionStatus() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    final BigDecimal amount = BigDecimal.valueOf(0.02);

    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(REVERSED)
            .transactionType(AUTHORISE)
            .amount(amount)
            .build();

    when(transactionRepository.findById(relatedTransaction.getId()))
        .thenReturn(java.util.Optional.empty());

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .relatedTransaction(relatedTransaction)
            .build();
    when(transactionRepository.findById(relatedTransaction.getId()))
        .thenReturn(java.util.Optional.of(relatedTransaction));
    when(transactionRepository.save(transactionEntityArgumentCaptor.capture()))
        .thenReturn(transaction);
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(amount)
            .relatedTransaction(mapper.toDto(relatedTransaction))
            .build();
    TransactionResponseDto saved = transactionService.createTransaction(transactionDto);
    assertThat(
        transactionEntityArgumentCaptor.getValue().getTransactionStatus(),
        is(TransactionStatuses.ERROR));
  }

  @Test
  @DisplayName("Create transaction will fail for invalid related transaction type")
  void createTransaction_willFail_for_invalidRelatedTransactionType() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    final BigDecimal amount = BigDecimal.valueOf(0.02);

    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final TransactionEntity relatedTransaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(REVERSAL)
            .amount(amount)
            .build();

    when(transactionRepository.findById(relatedTransaction.getId()))
        .thenReturn(java.util.Optional.empty());

    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .relatedTransaction(relatedTransaction)
            .build();
    when(transactionRepository.findById(relatedTransaction.getId()))
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
    assertThat(exception.getMessage(), is("Only authorize transactions can be charged."));
  }

  @Test
  @DisplayName("Create transaction will fail for missing related transaction")
  void createTransaction_willFail_for_missingRelatedTransaction() {
    final MerchantDto merchantDto = MerchantDto.builder().id(1L).build();
    when(merchantService.getActiveMerchant(Mockito.anyLong())).thenReturn(merchantDto);
    final TransactionEntity transaction =
        TransactionEntity.builder()
            .id(UUID.randomUUID())
            .transactionStatus(APPROVED)
            .transactionType(CHARGE)
            .build();
    when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(transaction);
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .transactionType(CHARGE)
            .transactionStatus(APPROVED)
            .merchant(merchantDto)
            .amount(BigDecimal.valueOf(0.02))
            .build();
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> transactionService.createTransaction(transactionDto));
    assertThat(exception.getMessage(), is("Charge transaction must have a related transaction"));
  }
}
