package com.emp.gw.task.mapper;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.model.entity.MerchantEntity;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MerchantMapperTest {

  private final MerchantMapper merchantMapper = Mappers.getMapper(MerchantMapper.class);

  @Test
  void toEntity() {
    final MerchantDto dto =
        MerchantDto.builder()
            .merchantName("m")
            .email("a@a.com")
            .description("desc")
            .active(true)
            .totalTransactionAmount(2.22)
            .id(22L)
            .build();
    final MerchantEntity entity =
        MerchantEntity.builder()
            .merchantName(dto.getMerchantName())
            .email(dto.getEmail())
            .description(dto.getDescription())
            .active(dto.isActive())
            .totalTransactionAmount(BigDecimal.valueOf(dto.getTotalTransactionAmount()))
            .id(dto.getId())
            .build();

    final MerchantDto inactiveDto =
        MerchantDto.builder()
            .merchantName("m")
            .email("a@a.com")
            .description("desc")
            .active(false)
            .totalTransactionAmount(2.22)
            .id(22L)
            .build();
    final MerchantEntity inactiveEntity =
        MerchantEntity.builder()
            .merchantName(inactiveDto.getMerchantName())
            .email(inactiveDto.getEmail())
            .description(inactiveDto.getDescription())
            .active(inactiveDto.isActive())
            .totalTransactionAmount(BigDecimal.valueOf(inactiveDto.getTotalTransactionAmount()))
            .id(inactiveDto.getId())
            .build();

    assertThat(merchantMapper.toEntity(dto), is(equalTo(entity)));
    assertThat(merchantMapper.toEntity(inactiveDto), is(equalTo(inactiveEntity)));
  }

  @Test
  void toDto() {
    final MerchantEntity entity =
        MerchantEntity.builder()
            .merchantName("h")
            .email("h@m.com")
            .description("desc")
            .active(true)
            .totalTransactionAmount(BigDecimal.valueOf(1.11))
            .id(11L)
            .build();
    final MerchantDto dto = MerchantDto.builder()
            .merchantName(entity.getMerchantName())
            .email(entity.getEmail())
            .description(entity.getDescription())
            .active(entity.isActive())
            .totalTransactionAmount(entity.getTotalTransactionAmount().doubleValue())
            .id(entity.getId())
            .build();

    final MerchantEntity inactiveEntity =
        MerchantEntity.builder()
            .merchantName("h")
            .email("h@m.com")
            .description("desc")
            .active(true)
            .totalTransactionAmount(BigDecimal.valueOf(1.11))
            .id(11L)
            .build();
    final MerchantDto inactiveDto = MerchantDto.builder()
        .merchantName(inactiveEntity.getMerchantName())
        .email(inactiveEntity.getEmail())
        .description(inactiveEntity.getDescription())
        .active(inactiveEntity.isActive())
        .totalTransactionAmount(inactiveEntity.getTotalTransactionAmount().doubleValue())
        .id(inactiveEntity.getId())
        .build();
    assertThat(merchantMapper.toDto(entity), is(equalTo(dto)));
    assertThat(merchantMapper.toDto(inactiveEntity), is(equalTo(inactiveDto)));
  }
}
