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

/**
 * Transaction cleanup service. Used to clean up old transactions.
 *
 * <p>Transaction cleanup - is a process that deletes all transactions older than 1 hour. It is
 * configured to run every hour at 21st minute and at 21st second. The scheduler is configured in
 * cron syntax.
 */
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
