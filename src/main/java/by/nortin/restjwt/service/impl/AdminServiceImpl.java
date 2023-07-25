package by.nortin.restjwt.service.impl;

import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.service.AdminService;
import by.nortin.restjwt.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserService userService;

    @Override
    public void setAdmin(Long id) {
        userService.setRoleAdmin(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
