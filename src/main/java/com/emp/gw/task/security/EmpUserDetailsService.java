package com.emp.gw.task.security;

import com.emp.gw.task.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** User details service implementation. Used to manipulate user entities in the database. */
@RequiredArgsConstructor
public class EmpUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User not found"));

    return new EmpUserDetails(user);
  }
}
