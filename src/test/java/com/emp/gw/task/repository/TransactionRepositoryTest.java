package com.emp.gw.task.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.dto.TransactionDto;
import com.emp.gw.task.enums.TransactionStatuses;
import com.emp.gw.task.enums.TransactionTypes;
import com.emp.gw.task.mapper.MerchantMapper;
import com.emp.gw.task.mapper.TransactionEntityMapper;
import com.emp.gw.task.model.entity.MerchantEntity;
import com.emp.gw.task.model.entity.TransactionEntity;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class TransactionRepositoryTest {

  @Autowired private TransactionRepository transactionRepository;
  @Autowired private MerchantRepository merchantRepository;

  private TransactionEntityMapper transactionMapper =
      Mappers.getMapper(TransactionEntityMapper.class);
  private MerchantMapper merchantMapper = Mappers.getMapper(MerchantMapper.class);

  @Container
  public static PostgreSQLContainer postgreSQLContainer =
      (PostgreSQLContainer)
          new PostgreSQLContainer("postgres:15.4")
              .withDatabaseName("task")
              .withUsername("postgres")
              .withPassword("postgres")
              .withReuse(true);

  @DynamicPropertySource
  static void postgresqlProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  @Test
  void testThatTransactionIsSaved() {
    transactionRepository.deleteAll();
    merchantRepository.deleteAll();

    final MerchantDto merchant = MerchantDto.builder().build();
    MerchantEntity merchantEntity = merchantRepository.save(merchantMapper.toEntity(merchant));

    TransactionDto dto =
        TransactionDto.builder()
            .transactionType(TransactionTypes.AUTHORISE)
            .amount(BigDecimal.valueOf(0.23))
            .transactionStatus(TransactionStatuses.APPROVED)
            .customerEmail("eml@a.b")
            .customerPhone("+3598966590909")
            .merchant(MerchantDto.builder().id(merchantEntity.getId()).build())
            .build();
    TransactionEntity transactionEntity = transactionMapper.toEntity(dto);
    TransactionEntity savedTransaction1 = transactionRepository.save(transactionEntity);
    List<TransactionEntity> entities = transactionRepository.findAll();
    assertThat(entities.size(), equalTo(1));

    transactionEntity = transactionMapper.toEntity(dto);
    transactionEntity.setRelatedTransaction(savedTransaction1);

    TransactionEntity savedTransaction2 = transactionRepository.save(transactionEntity);

    entities = transactionRepository.findAll();
    assertThat(entities.size(), equalTo(2));
    assertThat(savedTransaction2.getRelatedTransaction(), equalTo(savedTransaction1));
    assertThat(merchantRepository.findAll().size(), equalTo(1));
  }
}
