package com.emp.gw.task.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import com.emp.gw.task.exception.NotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

@TestInstance(Lifecycle.PER_CLASS)
class EmpUserDetailsServiceTest {

  private EmpUserDetailsService empUserDetailsService;

  @Mock private UserRepository userRepository;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    empUserDetailsService = new EmpUserDetailsService(userRepository);
  }

  @Test
  void loadUserByUsername_willSuccess() {
    final User user = new User();
    user.setUsername("test");
    user.setPassword("test");
    user.setAuthority("test");
    user.setCredentialsNonExpired(true);
    user.setEnabled(true);
    user.setId(1L);

    Mockito.when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
    UserDetails userDetails = empUserDetailsService.loadUserByUsername("test");
    assertThat(userDetails.getUsername(), equalTo("test"));
    assertThat(userDetails.getPassword(), equalTo("test"));
    assertThat(userDetails.getAuthorities().size(), equalTo(1));
    assertThat(
        userDetails.getAuthorities().stream().findFirst().get().getAuthority(), equalTo("test"));
    assertThat(userDetails.isCredentialsNonExpired(), equalTo(true));
    assertThat(userDetails.isAccountNonLocked(), equalTo(true));
    assertThat(userDetails.isAccountNonLocked(), equalTo(true));
    assertThat(userDetails.isAccountNonExpired(), equalTo(true));
  }

  @Test
  void loadUserByUsername_willThrowException() {
    Mockito.when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> empUserDetailsService.loadUserByUsername("test"));
  }
}
