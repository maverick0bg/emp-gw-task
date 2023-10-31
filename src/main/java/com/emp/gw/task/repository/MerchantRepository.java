package com.emp.gw.task.repository;

import com.emp.gw.task.model.entity.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

  boolean existsByEmail(String email);
}
