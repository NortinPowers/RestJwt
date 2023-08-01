package by.nortin.restjwt.service;

import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.dto.UserRegistrationDto;
import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    void saveUser(UserRegistrationDto user);

    boolean isUserExist(String userName);

    void setRoleAdmin(Long id);

    List<UserDto> getAllUsers();
}
