package com.emp.gw.task.service;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.enums.MerchantUpdatableFields;

import java.util.Map;

public interface MerchantService {

  MerchantDto createMerchant(MerchantDto merchantDto);

  MerchantDto getMerchant(Long merchantId);

  MerchantDto updateMerchant(Long merchantId, Map<MerchantUpdatableFields, Object> fields);
}
