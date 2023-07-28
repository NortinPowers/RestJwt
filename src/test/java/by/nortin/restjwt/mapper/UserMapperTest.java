package by.nortin.restjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.nortin.restjwt.domain.Role;
import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private final User user;
    private final UserDto userDto;
    private final UserRegistrationDto userRegistrationDto;

    {
        Long id = 1L;
        String username = "user";
        String password = "password";
        String roleName = "ROLE_USER";
        Role role = new Role();
        role.setId(id);
        role.setName(roleName);
        user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setPassword(password);
        user.setRole(role);
        userDto = new UserDto();
        userDto.setId(id);
        userDto.setUserName(username);
        userDto.setRole(roleName);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setUserName(username);
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setVerifyPassword(password);
    }

    @Test
    void test_convertToDto() {
        assertEquals(userDto, userMapper.convertToDto(user));
    }

    @Test
    void test_convertToDomain() {
        assertEquals(user.getUserName(), userMapper.convertToDomain(userRegistrationDto).getUserName());
        assertEquals(user.getPassword(), userMapper.convertToDomain(userRegistrationDto).getPassword());
    }
}
