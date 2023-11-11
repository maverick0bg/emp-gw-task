package com.emp.gw.task.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Merchant DTO. This class is used to transfer merchant data between the controller and the
 * service.
 */
@Builder
@Jacksonized
@Data
public class MerchantDto {
  private Long id;
  private @NotEmpty String merchantName;
  private @NotEmpty String description;
  private @NotEmpty String email;
  private boolean active;
  private double totalTransactionAmount;
}
