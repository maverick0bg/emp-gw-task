package com.emp.gw.task.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.enums.MerchantUpdatableFields;
import com.emp.gw.task.exception.ConflictException;
import com.emp.gw.task.exception.NotFoundException;
import com.emp.gw.task.mapper.MerchantMapper;
import com.emp.gw.task.model.entity.MerchantEntity;
import com.emp.gw.task.repository.MerchantRepository;
import com.emp.gw.task.service.impl.MerchantServiceImpl;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MerchantServiceTest {

  private MerchantService merchantService;
  @Mock MerchantRepository merchantRepository;
  @Mock MerchantMapper merchantMapper;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    merchantService = new MerchantServiceImpl(merchantRepository, merchantMapper);
  }

  @Test
  void createMerchant_success() {
    MerchantDto createRequestDto =
        MerchantDto.builder().merchantName("m").email("memail@a.com").build();
    when(merchantRepository.existsByEmail(createRequestDto.getEmail())).thenReturn(false);
    when(merchantMapper.toEntity(createRequestDto))
        .thenReturn(
            MerchantEntity.builder()
                .merchantName(createRequestDto.getMerchantName())
                .email(createRequestDto.getEmail())
                .build());
    final MerchantEntity savedMerchant =
        MerchantEntity.builder()
            .id(1L)
            .active(false)
            .email(createRequestDto.getEmail())
            .merchantName(createRequestDto.getMerchantName())
            .totalTransactionAmount(BigDecimal.valueOf(0))
            .build();
    when(merchantRepository.save(any(MerchantEntity.class))).thenReturn(savedMerchant);

    final MerchantDto resultMerchant =
        MerchantDto.builder()
            .id(savedMerchant.getId())
            .email(savedMerchant.getEmail())
            .merchantName(savedMerchant.getMerchantName())
            .active(savedMerchant.isActive())
            .totalTransactionAmount(savedMerchant.getTotalTransactionAmount().doubleValue())
            .description(savedMerchant.getDescription())
            .build();
    when(merchantMapper.toDto(any(MerchantEntity.class))).thenReturn(resultMerchant);
    MerchantDto result = merchantService.createMerchant(createRequestDto);
    assertThat(result, is(resultMerchant));
    verify(merchantRepository, times(1)).existsByEmail(createRequestDto.getEmail());
  }

  @Test
  void createMerchant_expectedConflictException() {
    MerchantDto createRequestDto =
        MerchantDto.builder().merchantName("n").email("nemail@a.com").build();
    when(merchantRepository.existsByEmail(createRequestDto.getEmail())).thenReturn(true);
    Assert.assertThrows(
        ConflictException.class, () -> merchantService.createMerchant(createRequestDto));
  }

  @Test
  void getMerchant_will_success() {
    final MerchantEntity foundMerchant =
        MerchantEntity.builder()
            .id(1L)
            .active(false)
            .email("f@a.com")
            .merchantName("f")
            .totalTransactionAmount(BigDecimal.valueOf(0))
            .description("desc")
            .build();
    when(merchantRepository.findById(1L)).thenReturn(java.util.Optional.of(foundMerchant));
    final MerchantDto expectedMerchant =
        MerchantDto.builder()
            .id(foundMerchant.getId())
            .email(foundMerchant.getEmail())
            .merchantName(foundMerchant.getMerchantName())
            .active(foundMerchant.isActive())
            .totalTransactionAmount(foundMerchant.getTotalTransactionAmount().doubleValue())
            .description(foundMerchant.getDescription())
            .build();
    when(merchantMapper.toDto(foundMerchant)).thenReturn(expectedMerchant);
    MerchantDto result = merchantService.getMerchant(1L);
    assertThat(result, is(expectedMerchant));
  }

  @Test
  void getMerchant_will_throwNotFound() {
    when(merchantRepository.findById(1L)).thenReturn(java.util.Optional.empty());
    Assert.assertThrows(NotFoundException.class, () -> merchantService.getMerchant(1L));
  }

  @Test
  void updateMerchant() {
    MerchantEntity foundEntity =
        MerchantEntity.builder()
            .id(10L)
            .active(false)
            .email("o@a.com")
            .merchantName("o")
            .totalTransactionAmount(BigDecimal.valueOf(0))
            .description("desc")
            .build();
    when(merchantRepository.findById(10L)).thenReturn(java.util.Optional.of(foundEntity));
    Map<MerchantUpdatableFields, Object> updateRequestDto =
        Map.of(MerchantUpdatableFields.ACTIVE, Boolean.valueOf(true));
    final MerchantEntity updatedEntity =
        MerchantEntity.builder()
            .id(10L)
            .active(true)
            .email("o@a.com")
            .merchantName("o")
            .totalTransactionAmount(BigDecimal.valueOf(0))
            .description("desc")
            .build();
    when(merchantRepository.save(foundEntity)).thenReturn(updatedEntity);
    final MerchantDto resultMerchant =
        MerchantDto.builder()
            .id(10L)
            .active(true)
            .email("o@a.com")
            .merchantName("o")
            .totalTransactionAmount(0)
            .description("desc")
            .build();
    when(merchantMapper.toDto(updatedEntity)).thenReturn(resultMerchant);
    MerchantDto result = merchantService.updateMerchant(10L, updateRequestDto);

    assertThat(result, is(resultMerchant));
  }

  @Test
  void updateMerchant_trowsNotFound() {
    when(merchantRepository.findById(11L)).thenReturn(java.util.Optional.empty());
    Assert.assertThrows(NotFoundException.class, () -> merchantService.getMerchant(11L));
  }
}
