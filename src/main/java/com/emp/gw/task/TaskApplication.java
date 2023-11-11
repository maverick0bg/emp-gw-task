package com.emp.gw.task;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Task application. This class is used to bootstrap the application.
 *
 * <p>SpringBootApplication annotation is used to enable auto-configuration, component scan and
 * configuration properties scanning.
 *
 * <p>EnableScheduling annotation is used to enable scheduling.
 *
 * <p>EnableSchedulerLock annotation is used to enable distributed lock for scheduled tasks.
 */
@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class TaskApplication {
  public static void main(String[] args) {
    SpringApplication.run(TaskApplication.class, args);
  }
}
