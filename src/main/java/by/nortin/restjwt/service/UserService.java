package by.nortin.restjwt.service;

import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.dto.UserRegistrationDto;
import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

//    void saveUser(UserDto user);
    void saveUser(UserRegistrationDto user);

    boolean isUserExist(String userName);
//    boolean isUserExist(UserRegistrationDto user);

    void setRoleAdmin(Long id);

    List<UserDto> getAllUsers();
}
