package by.nortin.restjwt.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.nortin.restjwt.domain.Role;
import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserRegistrationDto;
import by.nortin.restjwt.mapper.UserMapper;
import by.nortin.restjwt.repository.RoleRepository;
import by.nortin.restjwt.repository.UserRepository;
import by.nortin.restjwt.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private RoleRepository roleRepository;

    private final User user;
    private final Role role;

    {
        user = new User();
        user.setId(1L);
        user.setUserName("User");
        user.setPassword("Password");
        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        user.setRole(role);
    }

    @Nested
    class TestGetUserByUsername {

        @Test
        void test_getUserByUsername_getUser() {
            when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));

            User userByUsername = userService.getUserByUsername(user.getUserName());

            assertEquals(userByUsername, user);
            verify(userRepository, atLeastOnce()).findByUserName(user.getUserName());
        }

        @Test
        void test_getUserByUsername_getException() {
            when(userRepository.findByUserName(any())).thenThrow(new UsernameNotFoundException("not matter"));

            assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername(any()));
            verify(userRepository, atLeastOnce()).findByUserName(any());
        }
    }

    @Nested
    class TestSaveUser {

        private final UserRegistrationDto userRegistrationDto;

        {
            userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setUserName(user.getUserName());
            userRegistrationDto.setPassword(user.getPassword());
            userRegistrationDto.setVerifyPassword(user.getPassword());
        }

        @Test
        void test_saveUser_success() {
            user.setRole(null);
            String notEncryptedPassword = userRegistrationDto.getPassword();

            when(userMapper.convertToDomain(userRegistrationDto)).thenReturn(user);
            when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
            when(userRepository.save(user)).thenReturn(user);

            userService.saveUser(userRegistrationDto);

            assertEquals(user.getRole(), role);
            assertNotEquals(userRegistrationDto.getPassword(), notEncryptedPassword);
            verify(userMapper, atLeastOnce()).convertToDomain(userRegistrationDto);
            verify(roleRepository, atLeastOnce()).findByName(user.getRole().getName());
            verify(userRepository, atLeastOnce()).save(user);
        }

        @Test
        void test_saveUser_roleNotFound() {
            when(userMapper.convertToDomain(userRegistrationDto)).thenReturn(user);
            when(roleRepository.findByName(user.getRole().getName())).thenReturn(Optional.empty());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.saveUser(userRegistrationDto));
            verify(userMapper, atLeastOnce()).convertToDomain(userRegistrationDto);
            verify(roleRepository, atLeastOnce()).findByName(user.getRole().getName());
            verify(userRepository, never()).save(user);
        }
    }

    @Nested
    class TestIsUserExist {

        private Optional<User> optionalUser;

        @Test
        void test_isUserExist_true() {
            optionalUser = Optional.of(user);

            when(userRepository.findByUserName(any())).thenReturn(optionalUser);

            assertTrue(userService.isUserExist(any()));
            verify(userRepository, atLeastOnce()).findByUserName(any());
        }

        @Test
        void test_isUserExist_false() {
            optionalUser = Optional.empty();

            when(userRepository.findByUserName(any())).thenReturn(optionalUser);

            assertFalse(userService.isUserExist(any()));
            verify(userRepository, atLeastOnce()).findByUserName(any());
        }
    }

    /*
     @Override
    public void setRoleAdmin(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Optional<Role> optionalRole = roleRepository.findByName("ROLE_ADMIN");
            if (optionalRole.isPresent()) {
                User user = optionalUser.get();
                Role role = optionalRole.get();
                user.setRole(role);
                userRepository.save(user);
            } else {
                throw new DataSourceLookupFailureException("DataSource could not be obtained");
            }
        }
    }
     */
    @Nested
    class TestSetRoleAdmin {

        @Test
        void test_setRoleAdmin_success() {
            Role roleAdmin = new Role();
            roleAdmin.setId(2L);
            String roleAdminName = "ROLE_ADMIN";
            roleAdmin.setName(roleAdminName);

            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            when(roleRepository.findByName(roleAdminName)).thenReturn(Optional.of(roleAdmin));
            when(userRepository.save(user)).thenReturn(user);

            userService.setRoleAdmin(user.getId());

            assertEquals(user.getRole().getName(), roleAdmin.getName());
            verify(userRepository, atLeastOnce()).findById(user.getId());
            verify(roleRepository, atLeastOnce()).findByName(roleAdminName);
            verify(userRepository, atLeastOnce()).save(user);
        }

        @Test
        void test_setRoleAdmin_userNotFound() {
        }

        @Test
        void test_setRoleAdmin_roleNotFound() {
        }
    }

    @Test
    void getAllUsers() {
    }
}
