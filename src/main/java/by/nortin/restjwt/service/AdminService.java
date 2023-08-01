package by.nortin.restjwt.service;

import by.nortin.restjwt.dto.UserDto;
import java.util.List;

public interface AdminService {

    void setAdmin(Long id);

    List<UserDto> getAllUsers();
}
