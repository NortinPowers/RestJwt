package by.nortin.restjwt.service.impl;

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
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
//    @Transactional
    public void saveUser(UserRegistrationDto userRegistrationDto) {
        User user = userMapper.convertToDomain(userRegistrationDto);
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        if (optionalRole.isPresent()) {
            user.setRole(optionalRole.get());
            userRepository.save(user);
        } else {
            //can be empty
            throw new DataSourceLookupFailureException("DataSource could not be obtained");
        }
    }

    @Override
    public boolean isUserExist(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }

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

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::convertToDto)
                .toList();
    }
}
