package com.emp.gw.task.model.entity;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class MerchantEntityTest {

  @Test
  void testEquals() {
    MerchantEntity merchant1 = new MerchantEntity();
    MerchantEntity merchant2 = new MerchantEntity();
    assertThat(merchant1, is(merchant2));

    merchant1.setId(1L);
    merchant2.setId(2L);
    assertThat(merchant1, is(not(merchant2)));
  }

  @Test
  void testHashCode() {
    MerchantEntity merchant1 = new MerchantEntity();
    MerchantEntity merchant2 = new MerchantEntity();
    assertThat(merchant1.hashCode(), is(merchant2.hashCode()));

    merchant1.setMerchantName("merchant1");
    merchant2.setMerchantName("merchant2");
    assertThat(merchant1.hashCode(), is(not(merchant2.hashCode())));
  }
}