package by.nortin.restjwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.JwtRequest;
import by.nortin.restjwt.security.CustomUserDetail;
import by.nortin.restjwt.token.JwtTokenManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtTokenManager jwtTokenManager;

    @MockBean
    private AuthenticationManager authenticationManager;

    private final String username;
    private final JwtRequest request;
    private final User user;

    {
        username = "testUser";
        String password = "testPassword";
        request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        user = new User();
        user.setUserName(username);
        user.setPassword(password);
        user.setRole(null);
    }

    @Test
    public void test_getToken_success() {
        UserDetails userDetails = new CustomUserDetail(user);
        String expectedToken = "testToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenManager.generateJwtToken(userDetails)).thenReturn(expectedToken);

        String token = authService.getToken(request);

        assertEquals(token, expectedToken);
        verify(authenticationManager, atLeastOnce()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, atLeastOnce()).loadUserByUsername(username);
        verify(jwtTokenManager, atLeastOnce()).generateJwtToken(userDetails);
    }

    @Test
    public void test_getToken_notAuthentication() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("not matter"));

        assertThrows(BadCredentialsException.class, () -> authService.getToken(request));
        verify(authenticationManager, atLeastOnce()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(jwtTokenManager);
    }

    @Test
    public void test_getToken_userNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("not matter"));

        assertThrows(UsernameNotFoundException.class, () -> authService.getToken(request));
        verify(authenticationManager, atLeastOnce()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, atLeastOnce()).loadUserByUsername(username);
        verifyNoInteractions(jwtTokenManager);
    }
}
