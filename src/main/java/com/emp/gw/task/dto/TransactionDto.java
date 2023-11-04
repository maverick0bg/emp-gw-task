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
import org.hibernate.validator.constraints.Length;

/** DTO for {@link com.emp.gw.task.model.entity.TransactionEntity}. */
@Builder
@Jacksonized
@Data
public class TransactionDto {

  UUID id;
  @NotNull(message = "Transaction type can't be null") TransactionTypes transactionType;
  BigDecimal amount;
  TransactionStatuses transactionStatus;

  @Email
  @Length(min = 0, max = 255, message = "Customer e-mail must be between 0 and 255 characters")
  @NotNull(message = "Customer e-mail can't be null")
  String customerEmail;

  @Length(min = 0, max = 20, message = "Customer phone must be between 0 and 20 characters")
  @NotNull(message = "Customer phone can't be null")
  String customerPhone;

  TransactionDto relatedTransaction;
  @NotNull MerchantDto merchant;
}
