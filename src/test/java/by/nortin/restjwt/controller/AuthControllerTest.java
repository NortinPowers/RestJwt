package by.nortin.restjwt.controller;

import static by.nortin.restjwt.utils.ResponseUtils.BAD_CREDENTIALS_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.getExceptionResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.nortin.restjwt.dto.JwtRequest;
import by.nortin.restjwt.dto.JwtResponse;
import by.nortin.restjwt.model.ErrorValidationResponse;
import by.nortin.restjwt.model.ExceptionResponse;
import by.nortin.restjwt.service.AuthService;
import by.nortin.restjwt.service.UserService;
import by.nortin.restjwt.utils.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @Nested
    class TestCreateToken {

        private final String url = "/auth";
        private final ObjectMapper mapper = ResponseUtils.getObjectMapperWithTimeModule();

        private final JwtRequest jwtRequest = new JwtRequest();

        @Test
        void test_createAuthToken_success() throws Exception {
            String token = "header.payload.signature";
            jwtRequest.setUsername("user");
            jwtRequest.setPassword("password");
            JwtResponse response = new JwtResponse(token);

            when(authService.getToken(any())).thenReturn(token);

            mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        void test_createAuthToken_emptyBody() throws Exception {
            List<String> errors = List.of("Enter password", "Enter username");
            ErrorValidationResponse response = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        void test_createAuthToken_semiEmptyBody() throws Exception {
            List<String> errors = List.of("Enter password");
            jwtRequest.setUsername("user");
            ErrorValidationResponse response = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        record IncorrectJwtRequest(String name, String password) {
        }

        @Test
        void test_createAuthToken_incorrectBody() throws Exception {
            IncorrectJwtRequest incorrectJwtRequest = new IncorrectJwtRequest("user", "password");
            List<String> errors = List.of("Enter username");
            ErrorValidationResponse response = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(incorrectJwtRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        void test_createAuthToken_badCredentials() throws Exception {
            jwtRequest.setUsername("user");
            jwtRequest.setPassword("password");
            BadCredentialsException exception = new BadCredentialsException("");
            ExceptionResponse response = getExceptionResponse(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS_EXCEPTION_MESSAGE, exception);
//            ExceptionResponse response = new ExceptionResponse(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS_EXCEPTION_MESSAGE, "BadCredentialsException");

            when(authService.getToken(any())).thenThrow(exception);

            mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }

    @Test
    void createNewUser() {
    }
}