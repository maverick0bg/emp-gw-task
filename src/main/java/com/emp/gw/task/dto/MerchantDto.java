package com.emp.gw.task.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

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
