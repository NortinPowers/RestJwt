package by.nortin.restjwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.nortin.restjwt.domain.Role;
import by.nortin.restjwt.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserService userService;

    private final User user;

    {
        Long id = 1L;
        String username = "user";
        String password = "password";
        String roleName = "ROLE_USER";
        Role role = new Role();
        role.setId(id);
        role.setName(roleName);
        user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setPassword(password);
        user.setRole(role);
    }

    @Test
    void test_loadUserByUsername_isPresent() {
        when(userService.getUserByUsername(user.getUserName())).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUserName());

        assertEquals(userDetails.getUsername(), user.getUserName());
        verify(userService, atLeastOnce()).getUserByUsername(user.getUserName());

    }

    @Test
    void test_loadUserByUsername_isNotPresent() {
        when(userService.getUserByUsername(user.getUserName())).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(user.getUserName()));
        verify(userService, atLeastOnce()).getUserByUsername(user.getUserName());

    }
}
