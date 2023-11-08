package com.emp.gw.task.controller;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class IntegrationTestBase {
  @Container
  public static final PostgreSQLContainer postgreSQLContainer =
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

  static {
    postgreSQLContainer.start();
  }
}
