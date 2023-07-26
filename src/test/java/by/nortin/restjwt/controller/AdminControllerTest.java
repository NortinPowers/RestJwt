package by.nortin.restjwt.controller;

import static by.nortin.restjwt.utils.ResponseUtils.getObjectMapperWithTimeModule;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.model.ExceptionResponse;
import by.nortin.restjwt.service.AdminService;
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

    /*
     public ResponseEntity<BaseResponse> setAdmin(@PathVariable("id") @Min(1) Long id) {
        adminService.setAdmin(id);
        return ResponseEntity.ok(getSuccessResponse(CHANGE_ROLE_MESSAGE, User.class));
    }
     */

    @Nested
    class TestSetAdmin{

        private final String url = "/admin/set/{id}";
        private final Long id = 1L;

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
            ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.FORBIDDEN.value(), "Access Denied", "AccessDeniedException");
            ObjectMapper mapper = getObjectMapperWithTimeModule();
//            String accessDeniedExceptionJson = "{\"status\":403,\"timestamp\":\"2023-07-26\",\"message\":\"Access Denied\",\"type\":\"AccessDeniedException\"}";

            mockMvc.perform(patch(url, id))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(mapper.writeValueAsString(exceptionResponse)));
//                    .andExpect(content().json(accessDeniedExceptionJson));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_setAdmin_roleAdmin_success() throws Exception {
            doNothing().when(adminService).setAdmin(id);

            mockMvc.perform(patch(url, id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(ResponseUtils.CHANGE_ROLE_MESSAGE, "user")));
        }
    }

    @Nested
    class TestGetAllUser {

        private final String url = "/admin/users";
        private final ObjectMapper mapper = getObjectMapperWithTimeModule();

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
            ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.FORBIDDEN.value(), "Access Denied", "AccessDeniedException");

            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(mapper.writeValueAsString(exceptionResponse)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_getAllUser_roleAdmin_denied() throws Exception {
            UserDto firstUser = new UserDto();
            firstUser.setId(1L);
            firstUser.setUserName("One");
            firstUser.setRole("ROLE_USER");
            UserDto secondUser = new UserDto();
            secondUser.setId(1L);
            secondUser.setUserName("One");
            secondUser.setRole("ROLE_USER");
            List<UserDto> users = List.of(firstUser, secondUser);

            when(adminService.getAllUsers()).thenReturn(users);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(users)));
        }
    }
}