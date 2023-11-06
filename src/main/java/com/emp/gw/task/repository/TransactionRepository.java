package com.emp.gw.task.repository;

import com.emp.gw.task.model.entity.TransactionEntity;
import java.time.OffsetDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
  int deleteByCreatedDateBefore(OffsetDateTime olderThan);
}
