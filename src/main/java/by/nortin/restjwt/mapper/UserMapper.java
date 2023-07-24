package by.nortin.restjwt.mapper;

import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = BaseMapper.class)
public interface UserMapper {

//    @Mapping(target = "role", source = "user.role.name")
    UserDto convertToDto(User user);

//    @InheritInverseConfiguration
//    @Mapping(target = "role", expression = "java(new Role(dto.getRole()))")
    User convertToDomain(UserDto userDto);

//    User convertToDomain(UserRegistrationDto userRegistrationDto);
}
