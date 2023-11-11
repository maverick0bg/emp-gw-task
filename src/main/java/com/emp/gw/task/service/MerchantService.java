package com.emp.gw.task.service;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.enums.MerchantUpdatableFields;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Merchant service. This interface is used to define the contract for accessing and manipulating
 * merchant entities.
 */
public interface MerchantService {

  MerchantDto createMerchant(MerchantDto merchantDto);

  MerchantDto findMerchant(Long merchantId);

  MerchantDto getActiveMerchant(Long merchantId);

  MerchantDto updateMerchant(Long merchantId, Map<MerchantUpdatableFields, Object> fields);

  void addAmountToMerchantBalance(Long id, BigDecimal amount);

  void deleteMerchant(Long merchantId);

  void subtractAmountFromMerchantBalance(Long id, BigDecimal amount);
}
