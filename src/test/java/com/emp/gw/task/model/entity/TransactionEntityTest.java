package com.emp.gw.task.model.entity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class TransactionEntityTest {

  @Test
  void testEquals() {
    TransactionEntity transaction1 = new TransactionEntity();
    TransactionEntity transaction2 = new TransactionEntity();
    assertThat(transaction1, is(transaction2));

    transaction2.setId(UUID.randomUUID());
    transaction1.setId(UUID.randomUUID());
    assertThat(transaction1, is(not(transaction2)));
  }

  @Test
  void testHashCode() {
    TransactionEntity transaction1 = new TransactionEntity();
    TransactionEntity transaction2 = new TransactionEntity();
    assertThat(transaction1.hashCode(), is(transaction2.hashCode()));

    transaction1.setCustomerEmail("email1");
    transaction2.setCustomerEmail("email2");
    assertThat(transaction1.hashCode(), is(not(transaction2.hashCode())));
  }
}
