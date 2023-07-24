package by.nortin.restjwt.service;

import by.nortin.restjwt.dto.JwtRequest;
import by.nortin.restjwt.token.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;

    public String getToken(JwtRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return jwtTokenManager.generateJwtToken(userDetails);
    }
}
