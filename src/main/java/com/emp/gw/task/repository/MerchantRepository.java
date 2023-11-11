package com.emp.gw.task.repository;

import com.emp.gw.task.model.entity.MerchantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Merchant repository. This interface is used to access and manipulate merchant entities. */
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

  boolean existsByEmail(String email);

  Optional<MerchantEntity> findByIdAndActiveTrue(Long id);
}
