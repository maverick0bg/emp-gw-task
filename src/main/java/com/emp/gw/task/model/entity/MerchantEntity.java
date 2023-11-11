package com.emp.gw.task.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Merchant entity. This ORM class is used to map the merchants table from the database to a Java
 * class.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "merchants")
public class MerchantEntity implements Serializable {
  private static final long serialVersionUID = 165434343465L;

  @Id
  @SequenceGenerator(
      name = "merchant_generator",
      sequenceName = "merchants_seq",
      allocationSize = 1)
  @GeneratedValue(generator = "merchant_generator", strategy = GenerationType.SEQUENCE)
  @JdbcTypeCode(SqlTypes.BIGINT)
  private Long id;

  private String merchantName;
  private String description;
  private String email;
  private boolean active;
  private BigDecimal totalTransactionAmount;
  private Long userId;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "merchant")
  private List<TransactionEntity> transactions;
}
