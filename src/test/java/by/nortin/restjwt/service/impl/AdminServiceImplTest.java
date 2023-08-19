package by.nortin.restjwt.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.service.AdminService;
import by.nortin.restjwt.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

@SpringBootTest
class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;

    @MockBean
    private UserService userService;

    @Nested
    class TestSetAdmin {

        @Test
        void test_setAdmin_success() {
            doNothing().when(userService).setRoleAdmin(any());

            adminService.setAdmin(any());

            verify(userService, atLeastOnce()).setRoleAdmin(any());
        }

        @Test
        void test_setAdmin_dataSourceException() {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("not matter");

            doThrow(exception).when(userService).setRoleAdmin(any());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.setRoleAdmin(any()));

            verify(userService, atLeastOnce()).setRoleAdmin(any());
        }
    }

    @Nested
    class TestGetAllUsers {

        private final List<UserDto> users;

        {
            users = new ArrayList<>();
        }

        @Test
        void test_getAllUsers_getEmptyList() {
            when(userService.getAllUsers()).thenReturn(users);

            List<UserDto> allUsers = adminService.getAllUsers();

            assertEquals(allUsers, users);
            verify(userService, atLeastOnce()).getAllUsers();
        }

        @Test
        void test_getAllUsers_getNotEmptyList() {
            UserDto userDto = new UserDto();
            userDto.setId(1L);
            userDto.setUserName("User");
            userDto.setRole("ROLE_USER");
            users.add(userDto);

            when(userService.getAllUsers()).thenReturn(users);

            List<UserDto> allUsers = adminService.getAllUsers();

            assertEquals(allUsers, users);
            verify(userService, atLeastOnce()).getAllUsers();
        }
    }
}
