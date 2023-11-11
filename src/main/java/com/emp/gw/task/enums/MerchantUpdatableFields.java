package com.emp.gw.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum for merchant updatable fields. Enumerate possible fields that can be updated for a merchant.
 */
@AllArgsConstructor
public enum MerchantUpdatableFields {
  ACTIVE("active"),
  DESCRIPTION("description"),
  MERCHANT_NAME("merchantName");

  @Getter String fieldName;
}
