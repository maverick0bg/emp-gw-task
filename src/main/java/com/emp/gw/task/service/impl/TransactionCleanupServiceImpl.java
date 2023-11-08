package com.emp.gw.task.service.impl;

import static java.time.temporal.ChronoUnit.HOURS;

import com.emp.gw.task.repository.TransactionRepository;
import com.emp.gw.task.service.TransactionsCleanupService;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionCleanupServiceImpl implements TransactionsCleanupService {

  private static final int TRANSACTION_RETENTION_PERIOD = 1;
  private final TransactionRepository transactionRepository;

  @Override
  @Scheduled(cron = "21 21 * * * *")
  @SchedulerLock(name = "cleanUpTransactions", lockAtMostFor = "PT10M")
  @Transactional(TxType.REQUIRES_NEW)
  public void cleanUpTransactions() {
    log.info("Start cleaning up transactions");
    int deleted =
        transactionRepository.deleteByCreatedDateBefore(
            OffsetDateTime.now().minus(TRANSACTION_RETENTION_PERIOD, HOURS));
    log.info("Deleted {} transactions", deleted);
  }
}
