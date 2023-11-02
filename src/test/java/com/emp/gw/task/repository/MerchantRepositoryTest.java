package com.emp.gw.task.repository;

import com.emp.gw.task.model.entity.MerchantEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class MerchantRepositoryTest {

  @Container
  public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15.4")
      .withDatabaseName("task")
      .withUsername("postgres")
      .withPassword("postgres");

  @Autowired
  private MerchantRepository merchantRepository;

  @Test
  void testThatMerchantDataIsLoaded() throws IOException {
    List<MerchantEntity> loadedMerchants = merchantRepository.findAll();
    final File file = ResourceUtils.getFile(
        "classpath:db/changelog/2023/2023-01-merchants-data.csv");
    CsvSchema schema = CsvSchema.builder()
        .setUseHeader(true)
        .addColumn("merchant_name")
        .addColumn("description")
        .addColumn("email")
        .build();

    MappingIterator<Object> csvMerchants = new CsvMapper().reader(schema).forType(
        new TypeReference<Map<String, String>>() {
        }).readValues(
        file);
    assertThat(loadedMerchants.size(), equalTo(csvMerchants.readAll().size()));
  }

  @DynamicPropertySource
  static void postgresqlProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

}
