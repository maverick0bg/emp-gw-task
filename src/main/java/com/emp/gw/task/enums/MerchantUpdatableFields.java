package com.emp.gw.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MerchantUpdatableFields {
  ACTIVE("active"),
  DESCRIPTION("description"),
  MERCHANT_NAME("merchantName");

  @Getter String fieldName;
}
