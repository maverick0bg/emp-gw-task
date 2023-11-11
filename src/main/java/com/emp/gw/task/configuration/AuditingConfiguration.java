package com.emp.gw.task.configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for auditing.
 * It configures the date time provider to use UTC time zone.
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "utcDateTimeProvider")
@EnableTransactionManagement
public class AuditingConfiguration {
  @Bean
  public DateTimeProvider utcDateTimeProvider() {
    return () -> Optional.of(LocalDateTime.now(ZoneOffset.UTC).atOffset(ZoneOffset.UTC));
  }

}
