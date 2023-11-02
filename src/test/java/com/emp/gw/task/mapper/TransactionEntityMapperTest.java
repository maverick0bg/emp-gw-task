package com.emp.gw.task.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.enums.TransactionStatuses;
import com.emp.gw.task.enums.TransactionTypes;
import com.emp.gw.task.model.entity.TransactionEntity;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TransactionEntityMapperTest {

  TransactionEntityMapper transactionEntityMapper =
      Mappers.getMapper(TransactionEntityMapper.class);

  @Test
  void toEntity() {
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .id(UUID.randomUUID())
            .transactionType(TransactionTypes.AUTHORISE)
            .amount(BigDecimal.valueOf(0.12))
            .transactionStatus(TransactionStatuses.APPROVED)
            .customerEmail("c1@eml.com")
            .customerPhone("+359896658193")
            .relatedTransaction(null)
            .merchant(MerchantDto.builder().id(1L).build())
            .build();
    TransactionEntity entity = transactionEntityMapper.toEntity(transactionDto);
    assertThat(entity.getId(), is(transactionDto.getId()));
    assertThat(entity.getTransactionType(), is(transactionDto.getTransactionType()));
    assertThat(entity.getAmount(), is(transactionDto.getAmount()));
    assertThat(entity.getTransactionStatus(), is(transactionDto.getTransactionStatus()));
    assertThat(entity.getCustomerEmail(), is(transactionDto.getCustomerEmail()));
    assertThat(entity.getCustomerPhone(), is(transactionDto.getCustomerPhone()));
    assertThat(entity.getRelatedTransaction(), is(transactionDto.getRelatedTransaction()));
    assertThat(entity.getMerchant().getId(), is(transactionDto.getMerchant().getId()));
  }

  @Test
  void toDto() {
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .id(UUID.randomUUID())
            .transactionType(TransactionTypes.REFUND)
            .amount(BigDecimal.valueOf(0.12))
            .transactionStatus(TransactionStatuses.ERROR)
            .customerEmail("c1@eml.com")
            .customerPhone("+359896658193")
            .relatedTransaction(null)
            .merchant(MerchantDto.builder().id(1L).build())
            .build();
    final TransactionEntity transactionEntity = transactionEntityMapper.toEntity(transactionDto);
    TransactionDto dto = transactionEntityMapper.toDto(transactionEntity);
    assertThat(dto, is(transactionDto));
  }

  @Test
  void partialUpdate() {
    final TransactionDto transactionDto =
        TransactionDto.builder()
            .id(UUID.randomUUID())
            .transactionType(TransactionTypes.CHARGE)
            .amount(BigDecimal.valueOf(0.12))
            .transactionStatus(TransactionStatuses.REFUNDED)
            .customerEmail("c2@eml.com")
            .relatedTransaction(TransactionDto.builder().id(UUID.randomUUID()).build())
            .customerPhone("+359123456798")
            .merchant(MerchantDto.builder().id(1L).build()).build();
    final TransactionEntity transactionEntity = transactionEntityMapper.toEntity(transactionDto);
    transactionDto.setTransactionStatus(TransactionStatuses.REVERSED);
    transactionDto.setTransactionType(TransactionTypes.REVERSAL);
    TransactionEntity transformed =
        transactionEntityMapper.partialUpdate(transactionDto, transactionEntity);
    assertThat(transformed.getTransactionStatus(), is(transactionDto.getTransactionStatus()));
    assertThat(transformed.getTransactionType(), is(transactionDto.getTransactionType()));
  }
}
