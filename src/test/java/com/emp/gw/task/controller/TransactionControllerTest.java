package com.emp.gw.task.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.emp.gw.task.TaskApplication;
import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.enums.TransactionStatuses;
import com.emp.gw.task.enums.TransactionTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
@ContextConfiguration(classes = {TaskApplication.class})
class TransactionControllerTest extends IntegrationTestBase {

  public static final String TRANSACTIONS = "/transactions";
  @Autowired MockMvc mockMvc;
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
  void createTransaction() throws Exception {
    TransactionDto transaction =
        TransactionDto.builder()
            .customerEmail("e@dddd.com")
            .customerPhone("1234567890")
            .merchant(MerchantDto.builder().id(1L).build())
            .amount(BigDecimal.ONE)
            .transactionStatus(TransactionStatuses.APPROVED)
            .transactionType(TransactionTypes.AUTHORISE)
            .build();
    mockMvc
        .perform(
            post(TRANSACTIONS)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(transaction))
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.transactionStatus").value(TransactionStatuses.APPROVED.toString()));
  }

  @Test
  @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_NONE")
  void createTransaction_will_failWithForbidden() throws Exception {
    TransactionDto transaction =
        TransactionDto.builder()
            .customerEmail("e@dddd.com")
            .customerPhone("1234567890")
            .merchant(MerchantDto.builder().id(1L).build())
            .amount(BigDecimal.ONE)
            .transactionStatus(TransactionStatuses.APPROVED)
            .transactionType(TransactionTypes.AUTHORISE)
            .build();
    mockMvc
        .perform(
            post(TRANSACTIONS)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(transaction))
                .contentType("application/json"))
        .andExpect(status().isForbidden());
  }
}
