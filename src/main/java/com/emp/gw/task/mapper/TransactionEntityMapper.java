package com.emp.gw.task.mapper;

import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.model.entity.TransactionEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface TransactionEntityMapper {

  TransactionEntity toEntity(TransactionDto transactionDto);

  TransactionDto toDto(TransactionEntity transactionEntity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  TransactionEntity partialUpdate(
      TransactionDto transactionDto,
      @MappingTarget TransactionEntity transactionEntity);
}
