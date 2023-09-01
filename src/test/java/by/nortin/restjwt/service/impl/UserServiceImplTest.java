package by.nortin.restjwt.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import by.nortin.restjwt.domain.Role;
import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.dto.UserRegistrationDto;
import by.nortin.restjwt.mapper.UserMapper;
import by.nortin.restjwt.repository.RoleRepository;
import by.nortin.restjwt.repository.UserRepository;
import by.nortin.restjwt.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

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

    @Test
    void test_getAllUsers_success() {
        List<User> users = List.of(user);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setRole(user.getRole().getName());
        List<UserDto> userDtos = List.of(userDto);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.convertToDto(user)).thenReturn(userDto);

        List<UserDto> allUsers = userService.getAllUsers();

        assertEquals(allUsers, userDtos);
        verify(userRepository, atLeastOnce()).findAll();
        verify(userMapper, atLeast(1)).convertToDto(user);
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

            when(passwordEncoder.encode(user.getPassword())).thenReturn("encrypted password");
            when(userMapper.convertToDomain(userRegistrationDto)).thenReturn(user);
            when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
            when(userRepository.save(user)).thenReturn(user);

            userService.saveUser(userRegistrationDto);

            assertEquals(user.getRole(), role);
            assertNotEquals(userRegistrationDto.getPassword(), notEncryptedPassword);
            verify(passwordEncoder, atLeastOnce()).encode(user.getPassword());
            verify(userMapper, atLeastOnce()).convertToDomain(userRegistrationDto);
            verify(roleRepository, atLeastOnce()).findByName(user.getRole().getName());
            verify(userRepository, atLeastOnce()).save(user);
        }

        @Test
        void test_saveUser_roleNotFound() {
            when(passwordEncoder.encode(user.getPassword())).thenReturn("encrypted password");
            when(userMapper.convertToDomain(userRegistrationDto)).thenReturn(user);
            when(roleRepository.findByName(user.getRole().getName())).thenReturn(Optional.empty());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.saveUser(userRegistrationDto));
            verify(passwordEncoder, atLeastOnce()).encode(user.getPassword());
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

    @Nested
    class TestSetRoleAdmin {

        private final Role roleAdmin;
        private final String roleAdminName;

        {
            roleAdminName = "ROLE_ADMIN";
            roleAdmin = new Role();
            roleAdmin.setId(2L);
            roleAdmin.setName(roleAdminName);
        }

        @Test
        void test_setRoleAdmin_success() {
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
            when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> userService.setRoleAdmin(user.getId()));
            verify(userRepository, atLeastOnce()).findById(user.getId());
            verifyNoInteractions(roleRepository);
            verify(userRepository, never()).save(user);
        }

        @Test
        void test_setRoleAdmin_roleNotFound() {
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            when(roleRepository.findByName(roleAdminName)).thenReturn(Optional.empty());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.setRoleAdmin(user.getId()));
            verify(userRepository, atLeastOnce()).findById(user.getId());
            verify(roleRepository, atLeastOnce()).findByName(roleAdminName);
            verify(userRepository, never()).save(user);
        }

    }
}
