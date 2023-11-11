package com.emp.gw.task.mapper;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.model.entity.MerchantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Merchant mapper. This class is used to map merchant DTOs to merchant entities and vice versa.
 */
@Mapper
public interface MerchantMapper {

  @Mapping(target = "userId", ignore = true)
  MerchantEntity toEntity(MerchantDto dto);

  MerchantDto toDto(MerchantEntity merchantEntity);
}
