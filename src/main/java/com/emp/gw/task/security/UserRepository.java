package com.emp.gw.task.security;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** User repository. This interface is used to access and manipulate user entities. */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
