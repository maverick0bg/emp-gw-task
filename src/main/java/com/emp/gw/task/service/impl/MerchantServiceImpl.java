package com.emp.gw.task.service.impl;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.enums.MerchantUpdatableFields;
import com.emp.gw.task.exception.ConflictException;
import com.emp.gw.task.exception.NotFoundException;
import com.emp.gw.task.mapper.MerchantMapper;
import com.emp.gw.task.model.entity.MerchantEntity;
import com.emp.gw.task.repository.MerchantRepository;
import com.emp.gw.task.service.MerchantService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;

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
  public MerchantDto getMerchant(Long merchantId) {

    final MerchantEntity merchantEntity =
        merchantRepository
            .findById(merchantId)
            .orElseThrow(() -> new NotFoundException("Merchant not found"));
    return merchantMapper.toDto(merchantEntity);
  }

  @Override
  public MerchantDto updateMerchant(Long merchantId, Map<MerchantUpdatableFields, Object> fields) {
    MerchantEntity merchantEntity =
        merchantRepository
            .findById(merchantId)
            .orElseThrow(() -> new NotFoundException("Merchant not found"));
    fields.forEach(
        (key, value) ->
            PropertyAccessorFactory.forBeanPropertyAccess(merchantEntity)
                .setPropertyValue(key.getFieldName(), value));
    MerchantEntity savedMerchantEntity = merchantRepository.save(merchantEntity);
    return merchantMapper.toDto(savedMerchantEntity);
  }
}
