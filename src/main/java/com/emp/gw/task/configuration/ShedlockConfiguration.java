package com.emp.gw.task.configuration;

import javax.sql.DataSource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShedlockConfiguration {
  @Bean
  public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);
  }
}
