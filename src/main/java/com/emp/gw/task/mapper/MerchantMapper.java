package com.emp.gw.task.mapper;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.model.entity.MerchantEntity;
import org.mapstruct.Mapper;

@Mapper
public interface MerchantMapper {

  MerchantEntity toEntity(MerchantDto dto);

  MerchantDto toDto(MerchantEntity merchantEntity);
}
