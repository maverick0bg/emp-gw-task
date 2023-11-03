package com.emp.gw.task.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
}
