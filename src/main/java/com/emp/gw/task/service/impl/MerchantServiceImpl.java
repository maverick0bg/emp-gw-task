package com.emp.gw.task.service.impl;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.enums.MerchantUpdatableFields;
import com.emp.gw.task.exception.ConflictException;
import com.emp.gw.task.exception.NotFoundException;
import com.emp.gw.task.mapper.MerchantMapper;
import com.emp.gw.task.model.entity.MerchantEntity;
import com.emp.gw.task.repository.MerchantRepository;
import com.emp.gw.task.service.MerchantService;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;

/**
 * Merchant service. Implements the specific logic for Merchant.
 *
 * <p>Merchant - has name, email, address, phone number and total transaction amount. Merchant can
 * be referenced from Transaction. Merchant can be deleted only if it has no transactions.
 */
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

  private final MerchantRepository merchantRepository;
  private final MerchantMapper merchantMapper;

  @Override
  public MerchantDto createMerchant(MerchantDto merchantDto) {
    if (merchantRepository.existsByEmail(merchantDto.getEmail())) {
      throw new ConflictException(
          String.format("Merchant with e-mail address %s already exists", merchantDto.getEmail()));
    }
    MerchantEntity merchantEntity = merchantMapper.toEntity(merchantDto);
    merchantEntity = merchantRepository.save(merchantEntity);
    return merchantMapper.toDto(merchantEntity);
  }

  @Override
  public MerchantDto findMerchant(Long merchantId) {

    final MerchantEntity merchantEntity = findMerchantEntity(merchantId);
    return merchantMapper.toDto(merchantEntity);
  }

  @Override
  public MerchantDto getActiveMerchant(Long merchantId) {

    final MerchantEntity merchantEntity =
        merchantRepository
            .findByIdAndActiveTrue(merchantId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "Merchant with id " + merchantId + " does not exists or is not active!"));
    return merchantMapper.toDto(merchantEntity);
  }

  @Override
  public MerchantDto updateMerchant(Long merchantId, Map<MerchantUpdatableFields, Object> fields) {
    MerchantEntity merchantEntity = findMerchantEntity(merchantId);
    fields.forEach(
        (key, value) ->
            PropertyAccessorFactory.forBeanPropertyAccess(merchantEntity)
                .setPropertyValue(key.getFieldName(), value));
    MerchantEntity savedMerchantEntity = merchantRepository.save(merchantEntity);
    return merchantMapper.toDto(savedMerchantEntity);
  }

  @Override
  public void addAmountToMerchantBalance(Long id, BigDecimal amount) {
    MerchantEntity merchant = findMerchantEntity(id);
    merchant.setTotalTransactionAmount(merchant.getTotalTransactionAmount().add(amount));
    merchantRepository.save(merchant);
  }

  @Override
  public void deleteMerchant(Long merchantId) {
    MerchantEntity merchant = findMerchantEntity(merchantId);
    if (Objects.isNull(merchant.getTransactions()) || merchant.getTransactions().isEmpty()) {
      merchantRepository.deleteById(merchantId);
    } else {
      throw new ConflictException("Merchant has transactions and cannot be deleted");
    }
  }

  @Override
  public void subtractAmountFromMerchantBalance(Long id, BigDecimal amount) {
    MerchantEntity merchant = findMerchantEntity(id);
    merchant.setTotalTransactionAmount(merchant.getTotalTransactionAmount().subtract(amount));
    merchantRepository.save(merchant);
  }

  @Override
  public Iterable<MerchantDto> findAllMerchants() {
    return merchantRepository.findAll().stream().map(merchantMapper::toDto).toList();
  }

  private MerchantEntity findMerchantEntity(Long id) {
    return merchantRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Merchant not found"));
  }
}
