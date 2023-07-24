package by.nortin.restjwt.controller;

import static by.nortin.restjwt.utils.ResponseUtils.CREATION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.getSuccessResponse;

import by.nortin.restjwt.dto.JwtRequest;
import by.nortin.restjwt.dto.JwtResponse;
import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.model.BaseResponse;
import by.nortin.restjwt.service.AuthService;
import by.nortin.restjwt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest request) throws BadCredentialsException {
        String token = authService.getToken(request);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registration")
    public ResponseEntity<BaseResponse> createNewUser(@RequestBody @Valid UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.ok(getSuccessResponse(CREATION_MESSAGE, "user"));
    }
}
