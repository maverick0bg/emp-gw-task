package com.emp.gw.task.dto;

import com.emp.gw.task.enums.TransactionStatuses;
import com.emp.gw.task.enums.TransactionTypes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/** DTO for {@link com.emp.gw.task.model.entity.TransactionEntity}. */
@Builder
@Jacksonized
@Data
public class TransactionDto {

  UUID id;
  TransactionTypes transactionType;
  BigDecimal amount;
  TransactionStatuses transactionStatus;
  @Email String customerEmail;
  String customerPhone;
  TransactionDto relatedTransaction;
  @NotNull MerchantDto merchant;
}
