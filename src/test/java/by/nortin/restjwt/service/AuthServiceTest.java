package by.nortin.restjwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.JwtRequest;
import by.nortin.restjwt.security.CustomUserDetail;
import by.nortin.restjwt.token.JwtTokenManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @Mock
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

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(null);
        given(userDetailsService.loadUserByUsername(username))
                .willReturn(userDetails);
        given(jwtTokenManager.generateJwtToken(userDetails))
                .willReturn(expectedToken);

        String actualToken = authService.getToken(request);

        assertEquals(expectedToken, actualToken);
        verify(authenticationManager, atLeastOnce()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, atLeastOnce()).loadUserByUsername(username);
        verify(jwtTokenManager, atLeastOnce()).generateJwtToken(userDetails);
    }

    @Test
    public void test_getToken_notAuthentication() {
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("not matter"));

        assertThrows(BadCredentialsException.class, () -> authService.getToken(request));

        verify(authenticationManager, atLeastOnce()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(jwtTokenManager);
    }

    @Test
    public void test_getToken_userNotFound() {
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(null);
        given(userDetailsService.loadUserByUsername(username))
                .willThrow(new UsernameNotFoundException("nor matter"));

        assertThrows(UsernameNotFoundException.class, () -> authService.getToken(request));

        verify(authenticationManager, atLeastOnce()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, atLeastOnce()).loadUserByUsername(username);
        verifyNoInteractions(jwtTokenManager);
    }
}
