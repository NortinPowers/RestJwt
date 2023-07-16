package by.nortin.restjwt.service;

import by.nortin.restjwt.domain.User;

public interface UserService {

    User getUserByUsername(String username);
}
