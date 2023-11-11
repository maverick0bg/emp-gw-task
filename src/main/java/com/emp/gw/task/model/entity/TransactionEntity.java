package com.emp.gw.task.model.entity;

import com.emp.gw.task.enums.TransactionStatuses;
import com.emp.gw.task.enums.TransactionTypes;
import com.emp.gw.task.validator.PhoneNumberConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

/**
 * Transaction entity. This ORM class is used to map the transactions table from the database to a
 * Java class.
 */
@Valid
@Entity(name = "transactions")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity extends AuditableEntity implements Serializable {

  private static final long serialVersionUID = 16543876543L;

  @UuidGenerator @Id private UUID id;

  @Transient private TransactionTypes transactionType;

  @Column(name = "transaction_type_id")
  private int transactionTypeId;

  private BigDecimal amount;

  @Transient private TransactionStatuses transactionStatus;

  @Column(name = "status_id")
  private int statusId;

  @Email private String customerEmail;
  @PhoneNumberConstraint
  private String customerPhone;

  @OneToOne
  @JoinColumn(name = "reference_id")
  private TransactionEntity relatedTransaction;

  @ManyToOne
  @JoinColumn(name = "merchant_id")
  private MerchantEntity merchant;

  @PrePersist
  public void prePersist() {
    if (transactionType != null) {
      transactionTypeId = transactionType.getType();
    }
    if (transactionStatus != null) {
      statusId = transactionStatus.getStatus();
    }
  }

  @PostLoad
  public void postLoad() {
    transactionType = TransactionTypes.getTransactionType(transactionTypeId);
    transactionStatus = TransactionStatuses.getTransactionStatus(statusId);
  }
}
