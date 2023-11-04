package com.emp.gw.task.dto;

import com.emp.gw.task.enums.TransactionStatuses;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponseDto {
  UUID id;
  TransactionStatuses transactionStatus;
}
