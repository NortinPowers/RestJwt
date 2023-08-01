package by.nortin.restjwt.controller;

import static by.nortin.restjwt.test.utils.ResponseUtils.CHANGE_ROLE_MESSAGE;
import static by.nortin.restjwt.test.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.test.utils.ResponseUtils.NOT_FOUND_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.test.utils.ResponseUtils.getExceptionResponse;
import static by.nortin.restjwt.test.utils.ResponseUtils.getObjectMapperWithTimeModule;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.model.ExceptionResponse;
import by.nortin.restjwt.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    private String url;
    private final ExceptionResponse exceptionResponse;

    private final ObjectMapper mapper;

    {
        mapper = getObjectMapperWithTimeModule();
    }

    {
        exceptionResponse = new ExceptionResponse(HttpStatus.FORBIDDEN, "Access Denied", "AccessDeniedException");
    }

    @Nested
    class TestSetAdmin {

        private final Long id;

        {
            url = "/admin/set/{id}";
            id = 1L;
        }

        @Test
        @WithAnonymousUser
        void test_setAdmin_anonymous_denied() throws Exception {
            mockMvc.perform(patch(url, id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access, you need to transfer a token."));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void test_setAdmin_roleUser_denied() throws Exception {
            mockMvc.perform(patch(url, id))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(mapper.writeValueAsString(exceptionResponse)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_setAdmin_roleAdmin_success() throws Exception {
            doNothing().when(adminService).setAdmin(id);

            mockMvc.perform(patch(url, id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(CHANGE_ROLE_MESSAGE, "user")));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_setAdmin_roleAdmin_dBError() throws Exception {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("it does not matter");
            ExceptionResponse response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE, exception);

            doThrow(exception).when(adminService).setAdmin(any());

            mockMvc.perform(patch(url, id))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_setAdmin_roleAdmin_entityNotFound() throws Exception {
            EntityNotFoundException exception = new EntityNotFoundException("it does not matter");
            ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_FOUND, NOT_FOUND_EXCEPTION_MESSAGE, exception);

            doThrow(exception).when(adminService).setAdmin(any());

            mockMvc.perform(patch(url, id))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }

    @Nested
    class TestGetAllUser {

        private final ObjectMapper mapper;

        {
            url = "/admin/users";
            mapper = getObjectMapperWithTimeModule();
        }

        @Test
        @WithAnonymousUser
        void test_getAllUser_anonymous_denied() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access, you need to transfer a token."));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void test_getAllUser_roleUser_denied() throws Exception {

            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(mapper.writeValueAsString(exceptionResponse)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_getAllUser_roleAdmin_alloyed() throws Exception {
            UserDto firstUser = new UserDto();
            firstUser.setId(1L);
            firstUser.setUserName("admin");
            firstUser.setRole("ROLE_ADMIN");
            UserDto secondUser = new UserDto();
            secondUser.setId(2L);
            secondUser.setUserName("user");
            secondUser.setRole("ROLE_USER");
            List<UserDto> users = List.of(firstUser, secondUser);

            when(adminService.getAllUsers()).thenReturn(users);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(users)));
        }
    }
}
