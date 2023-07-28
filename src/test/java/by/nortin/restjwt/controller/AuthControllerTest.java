package by.nortin.restjwt.controller;

import static by.nortin.restjwt.utils.ResponseUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.nortin.restjwt.dto.JwtRequest;
import by.nortin.restjwt.dto.JwtResponse;
import by.nortin.restjwt.dto.UserRegistrationDto;
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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    private final ObjectMapper mapper;
    private String url;
    private ErrorValidationResponse errorValidationResponse;
    private List<String> errors;
    private ExceptionResponse response;

    {
        mapper = ResponseUtils.getObjectMapperWithTimeModule();
    }
    @Nested
    class TestCreateToken {

//        private final String url = "/auth";

        private final JwtRequest jwtRequest;


        {
            url = "/auth";
            jwtRequest = new JwtRequest();
            jwtRequest.setUsername("user");
            jwtRequest.setPassword("password");
        }

        @Test
        void test_createAuthToken_success() throws Exception {
            String token = "header.payload.signature";
//            jwtRequest.setUsername("user");
//            jwtRequest.setPassword("password");
            JwtResponse response = new JwtResponse(token);

            when(authService.getToken(any())).thenReturn(token);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        void test_createAuthToken_emptyBody() throws Exception {
            errors = List.of("Enter password", "Enter username");
//            List<String> errors = List.of("Enter password", "Enter username");
            errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);
//            ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);
            jwtRequest.setUsername("");
            jwtRequest.setPassword("");

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        void test_createAuthToken_semiEmptyBody() throws Exception {
            errors = List.of("Enter password");
//            List<String> errors = List.of("Enter password");
//            jwtRequest.setUsername("user");
            jwtRequest.setPassword("");
            errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);
//            ErrorValidationResponse response = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        record IncorrectJwtRequest(String name, String password) {
        }

        @Test
        void test_createAuthToken_incorrectBody() throws Exception {
            IncorrectJwtRequest incorrectJwtRequest = new IncorrectJwtRequest("user", "password");
            errors = List.of("Enter username");
//            List<String> errors = List.of("Enter username");
            errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);
//            ErrorValidationResponse response = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(incorrectJwtRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        void test_createAuthToken_badCredentials() throws Exception {
//            jwtRequest.setUsername("user");
//            jwtRequest.setPassword("password");
            BadCredentialsException exception = new BadCredentialsException("it does not matter");
            response = getExceptionResponse(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS_EXCEPTION_MESSAGE, exception);
//            ExceptionResponse response = new ExceptionResponse(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS_EXCEPTION_MESSAGE, "BadCredentialsException");

            when(authService.getToken(any())).thenThrow(exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        void test_createAuthToken_dBError() throws Exception {
//            jwtRequest.setUsername("user");
//            jwtRequest.setPassword("password");
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("it does not matter");
            response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE, exception);
//            ExceptionResponse response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE, exception);

            when(authService.getToken(any())).thenThrow(exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        void test_createAuthToken_incorrectBodyType() throws Exception {
            String request = "user, password";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("it does not matter", new MockHttpInputMessage("it does not matter".getBytes()));
            response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, HTTP_NOT_READABLE_EXCEPTION_MESSAGE, exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }

    @Nested
    class TestCreateUser {

        private final String url;
        private final UserRegistrationDto userRegistrationDto;

        {
            url = "/auth/registration";
            userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setUserName("user");
            userRegistrationDto.setPassword("password");
            userRegistrationDto.setVerifyPassword("password");
        }

        @Test
        void test_createNewUser_success() throws Exception{
//            UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
//            userRegistrationDto.setUserName("user");
//            userRegistrationDto.setPassword("password");
//            userRegistrationDto.setVerifyPassword("password");

            doNothing().when(userService).saveUser(any());

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(userRegistrationDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value( String.format(CREATION_MESSAGE, "user")));
        }

        @Test
        void test_createNewUser_dBError() throws Exception{
//            UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
//            userRegistrationDto.setUserName("user");
//            userRegistrationDto.setPassword("password");
//            userRegistrationDto.setVerifyPassword("password");
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("it does not matter");
            response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE, exception);

            doThrow(exception).when(userService).saveUser(any());

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(userRegistrationDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        record IncorrectRegistrationBody(String name, String surname, String password) {
        }


        @Test
        void test_createNewUser_incorrectBody() throws Exception {
            IncorrectRegistrationBody incorrectRegistrationBody = new IncorrectRegistrationBody("test", "ester", "password");
            errors = List.of("Verify password","The entered passwords do not match","Enter username");
            errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(incorrectRegistrationBody)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        void test_createNewUser_passwordsNotMatching() throws Exception {
            userRegistrationDto.setVerifyPassword("incorrect");
            errors = List.of("The entered passwords do not match");
            errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(userRegistrationDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        void test_createNewUser_incorrectBodyType() throws Exception {
            String request = "user, password, password";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("it does not matter", new MockHttpInputMessage("it does not matter".getBytes()));
            response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, HTTP_NOT_READABLE_EXCEPTION_MESSAGE, exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }
}