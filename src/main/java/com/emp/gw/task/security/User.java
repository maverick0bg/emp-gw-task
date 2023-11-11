package com.emp.gw.task.security;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** User entity. This ORM class is used to map the users table from the database to a Java class. */
@Entity(name = "users")
@Getter
@Setter
public class User implements Serializable {

  private static final long serialVersionUID = 18765432345678L;

  @Id
  @SequenceGenerator(name = "user_generator", sequenceName = "users_seq", allocationSize = 1)
  @GeneratedValue(generator = "user_generator", strategy = GenerationType.SEQUENCE)
  @JdbcTypeCode(SqlTypes.BIGINT)
  private Long id;

  private String username;
  private String password;
  private boolean enabled;
  private String authority;
  private boolean credentialsNonExpired;
}
