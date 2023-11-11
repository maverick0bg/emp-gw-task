package com.emp.gw.task.dto;

import com.emp.gw.task.enums.TransactionStatuses;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Transaction response DTO. This class is used to transfer transaction data between the controller
 * and the service.
 */
@Data
@AllArgsConstructor
public class TransactionResponseDto {
  UUID id;
  TransactionStatuses transactionStatus;
}
