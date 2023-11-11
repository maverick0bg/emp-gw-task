package com.emp.gw.task.repository;

import com.emp.gw.task.model.entity.TransactionEntity;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Transaction repository. This interface is used to access and manipulate transaction entities. */
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
  int deleteByCreatedDateBefore(OffsetDateTime olderThan);

  Optional<TransactionEntity> findById(UUID transactionId);
}
