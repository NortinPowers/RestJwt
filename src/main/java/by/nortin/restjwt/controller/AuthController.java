package by.nortin.restjwt.controller;

import by.nortin.restjwt.dto.JwtRequest;
import by.nortin.restjwt.dto.JwtResponse;
import by.nortin.restjwt.service.CustomUserDetailsService;
import by.nortin.restjwt.service.UserService;
import by.nortin.restjwt.token.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest request) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenManager.generateJwtToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
