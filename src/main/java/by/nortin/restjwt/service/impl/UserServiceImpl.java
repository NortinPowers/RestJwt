package by.nortin.restjwt.service.impl;

import by.nortin.restjwt.domain.Role;
import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.mapper.UserMapper;
import by.nortin.restjwt.repository.RoleRepository;
import by.nortin.restjwt.repository.UserRepository;
import by.nortin.restjwt.service.UserService;
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
    public void saveUser(UserDto userDto) {
        User user = userMapper.convertToDomain(userDto);
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        if (optionalRole.isPresent()) {
            user.setRole(optionalRole.get());
        } else {
            throw new DataSourceLookupFailureException("DataSource could not be obtained");
        }
        userRepository.save(user);
    }

    @Override
    public boolean isUserExist(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }
}
