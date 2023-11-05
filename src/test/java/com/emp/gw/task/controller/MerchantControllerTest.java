package com.emp.gw.task.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.emp.gw.task.TaskApplication;
import com.emp.gw.task.dto.MerchantDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@WebAppConfiguration
@ContextConfiguration(classes = {TaskApplication.class})
class MerchantControllerTest {

  @Container
  public static PostgreSQLContainer postgreSQLContainer =
      new PostgreSQLContainer("postgres:15.4")
          .withDatabaseName("task")
          .withUsername("postgres")
          .withPassword("postgres");

  @DynamicPropertySource
  static void postgresqlProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  @Autowired MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();
  
  @Test
  @WithMockUser(username = "admin", password = "admin", roles = "admin")
  void createMerchant() throws Exception {
    final MerchantDto merchantRequest =
        MerchantDto.builder()
            .merchantName("MDB")
            .email("mdb@a.com")
            .description("MDB description")
            .build();
    mockMvc
        .perform(
            post("/merchant")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(merchantRequest))
                .contentType("application/json"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.merchantName").value(merchantRequest.getMerchantName()))
        .andExpect(jsonPath("$.email").value(merchantRequest.getEmail()))
        .andExpect(jsonPath("$.description").value(merchantRequest.getDescription()))
        .andExpect(jsonPath("$.active").value(false));
  }

  @Test
  @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
  void getMerchant() throws Exception {
    final MerchantDto merchantRequest =
        MerchantDto.builder()
            .merchantName("rtl")
            .email("rtl@a.com")
            .description("rtl description")
            .build();
    MvcResult mvcResult =
        mockMvc
            .perform(
                post("/merchant")
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin"))
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(merchantRequest))
                    .contentType("application/json"))
            .andExpect(status().isCreated())
            .andReturn();
    MerchantDto resultMerchant =
        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MerchantDto.class);
    mvcResult =
        mockMvc
            .perform(
                get("/merchant/" + resultMerchant.getId())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin"))
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

    assertThat(
        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MerchantDto.class),
        is(equalTo(resultMerchant)));
  }

  @Test
  @WithMockUser(username = "admin", password = "admin", roles = "admin")
  void updateMerchant() throws Exception {
    final MerchantDto merchantRequest =
        MerchantDto.builder()
            .merchantName("ktm")
            .email("ktm@a.com")
            .description("ktm description")
            .build();
    MvcResult mvcResult =
        mockMvc
            .perform(
                post("/merchant")
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin"))
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(merchantRequest))
                    .contentType("application/json"))
            .andExpect(status().isCreated())
            .andReturn();
    MerchantDto resultMerchant =
        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MerchantDto.class);

    assertThat(resultMerchant.isActive(), is(false));

    final String ktmDescriptionUpdated = "ktm description updated";
    final String ktmNameUpdated = "ktm updated";
    mockMvc
        .perform(
            patch("/merchant/" + resultMerchant.getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(
                    String.format(
                        """
                    {
                      "ACTIVE": true,
                      "DESCRIPTION": "%s",
                      "MERCHANT_NAME": "%s"
                    }
                    """,
                        ktmDescriptionUpdated, ktmNameUpdated))
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.active").value(true))
        .andExpect(jsonPath("$.description").value(ktmDescriptionUpdated))
        .andExpect(jsonPath("$.merchantName").value(ktmNameUpdated));
  }
}
