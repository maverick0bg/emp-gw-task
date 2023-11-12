package com.emp.gw.task.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
@ContextConfiguration(classes = {TaskApplication.class})
class MerchantControllerTest extends IntegrationTestBase {

  public static final String MERCHANTS = "/merchants";
  @Autowired MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
  void createMerchant() throws Exception {
    final MerchantDto merchantRequest =
        MerchantDto.builder()
            .merchantName("MDB")
            .email("mdb@a.com")
            .description("MDB description")
            .build();
    mockMvc
        .perform(
            post(MERCHANTS)
                .with(httpBasic("admin", "admin"))
                .with(csrf())
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
  @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_MERCHANT")
  void createMerchant_will_beRejectedForWrongRole() throws Exception {
    final MerchantDto merchantRequest =
        MerchantDto.builder()
            .merchantName("MDB")
            .email("mdb@a.com")
            .description("MDB description")
            .build();
    mockMvc
        .perform(
            post(MERCHANTS)
                .with(httpBasic("admin", "admin"))
                .with(csrf())
                .content(objectMapper.writeValueAsString(merchantRequest))
                .contentType("application/json"))
        .andExpect(status().isForbidden());
  }

  @Test
  void getMerchant_will_success_for_admin() throws Exception {
    final MerchantDto resultMerchant = createMerchantAsAdmin("eml1@mail.com");
    successfullyGetMerchant(resultMerchant, "admin", "admin", "ROLE_ADMIN");
  }

  @Test
  void getMerchant_will_success_for_merchant() throws Exception {
    final MerchantDto resultMerchant = createMerchantAsAdmin("eml2@mail.com");
    successfullyGetMerchant(resultMerchant, "merchant", "merchant", "ROLE_MERCHANT");
  }

  @Test
  void getMerchant_will_beRejected_for_other_role() throws Exception {
    final MerchantDto resultMerchant = createMerchantAsAdmin("eml3@mail.com");
    mockMvc
        .perform(
            get(MERCHANTS + "/" + resultMerchant.getId())
                .with(httpBasic("user", "password"))
                .with(csrf())
                .with(user("user").authorities(() -> "ROLE_USER"))
                .contentType("application/json"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
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
                post(MERCHANTS)
                    .with(httpBasic("admin", "admin"))
                    .with(csrf())
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
            patch(MERCHANTS + "/" + resultMerchant.getId())
                .with(httpBasic("admin", "admin"))
                .with(csrf())
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

  @Test
  @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
  void deleteMerchant() throws Exception {
    final MerchantDto merchantRequest =
        MerchantDto.builder()
            .merchantName("suzuki")
            .email("suzuki@a.com")
            .description("suzuki description")
            .build();
    MvcResult mvcResult =
        mockMvc
            .perform(
                post(MERCHANTS)
                    .with(httpBasic("admin", "admin"))
                    .with(csrf())
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
            patch(MERCHANTS + "/" + resultMerchant.getId())
                .with(httpBasic("admin", "admin"))
                .with(csrf())
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

    mockMvc
        .perform(
            delete(MERCHANTS + "/" + resultMerchant.getId())
                .with(httpBasic("admin", "admin"))
                .with(csrf())
                .contentType("application/json"))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  @DisplayName("Test DELETE '/merchant' endpoint will fail for existing transactions")
  @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
  void deleteMerchant_will_failForExistingTransactions() throws Exception {
    final MerchantDto merchantRequest =
        MerchantDto.builder()
            .merchantName("honda")
            .email("honda@a.com")
            .description("ktm description")
            .build();
    MvcResult mvcResult =
        mockMvc
            .perform(
                post(MERCHANTS)
                    .with(httpBasic("admin", "admin"))
                    .with(csrf())
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
            patch(MERCHANTS + "/" + resultMerchant.getId())
                .with(httpBasic("admin", "admin"))
                .with(csrf())
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

    TransactionDto transaction =
        TransactionDto.builder()
            .customerEmail("e@dddd.com")
            .customerPhone("1234567890")
            .merchant(MerchantDto.builder().id(resultMerchant.getId()).build())
            .amount(BigDecimal.ONE)
            .transactionStatus(TransactionStatuses.APPROVED)
            .transactionType(TransactionTypes.AUTHORISE)
            .build();
    mockMvc
        .perform(
            post("/transaction")
                .with(httpBasic("admin", "admin"))
                .with(csrf())
                .content(objectMapper.writeValueAsString(transaction))
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.transactionStatus").value(TransactionStatuses.APPROVED.toString()));

    mockMvc
        .perform(
            delete(MERCHANTS + "/" + resultMerchant.getId())
                .with(httpBasic("admin", "admin"))
                .with(csrf())
                .contentType("application/json"))
        .andExpect(status().isConflict());
  }

  private void successfullyGetMerchant(
      MerchantDto merchantForSearch, String user, String password, String authority)
      throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(
                get(MERCHANTS + "/" + merchantForSearch.getId())
                    .with(httpBasic(user, password))
                    .with(csrf())
                    .with(user(user).authorities(() -> authority))
                    .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

    assertThat(
        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MerchantDto.class),
        is(equalTo(merchantForSearch)));
  }

  private MerchantDto createMerchantAsAdmin(String email) throws Exception {
    final MerchantDto merchantRequest =
        MerchantDto.builder()
            .merchantName("rtl")
            .email(email)
            .description("rtl description")
            .build();
    MvcResult mvcResult =
        mockMvc
            .perform(
                post(MERCHANTS)
                    .with(httpBasic("admin", "admin"))
                    .with(csrf())
                    .with(user("admin").authorities(() -> "ROLE_ADMIN"))
                    .content(objectMapper.writeValueAsString(merchantRequest))
                    .contentType("application/json"))
            .andExpect(status().isCreated())
            .andReturn();
    MerchantDto resultMerchant =
        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MerchantDto.class);
    return resultMerchant;
  }
}
