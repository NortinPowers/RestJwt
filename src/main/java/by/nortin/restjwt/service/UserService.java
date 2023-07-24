package by.nortin.restjwt.service;

import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserDto;

public interface UserService {

    User getUserByUsername(String username);

    void saveUser(UserDto user);
//    void saveUser(UserRegistrationDto user);

    boolean isUserExist(String userName);
//    boolean isUserExist(UserRegistrationDto user);
}
