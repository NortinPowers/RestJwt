//package by.nortin.restjwt.service.impl;
//
//import by.nortin.restjwt.domain.Role;
//import by.nortin.restjwt.dto.RoleDto;
//import by.nortin.restjwt.mapper.RoleMapper;
//import by.nortin.restjwt.repository.RoleRepository;
//import by.nortin.restjwt.service.RoleService;
//import java.util.Optional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class RoleServiceImpl implements RoleService {
//
//    private final RoleRepository roleRepository;
//    private final RoleMapper roleMapper;
//
//    @Override
//    public Optional<RoleDto> getRoleBeName(String name) {
//        Optional<Role> roleOptional = roleRepository.findByName(name);
//        if (roleOptional.isPresent()) {
//            roleMapper.convertToDto(roleOptional.get());
//
//        }
//        return roleMapper.convertToDto(roleOptional);
//    }
//}
