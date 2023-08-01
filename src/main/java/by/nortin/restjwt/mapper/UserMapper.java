package by.nortin.restjwt.mapper;

import by.nortin.restjwt.domain.User;
import by.nortin.restjwt.dto.UserDto;
import by.nortin.restjwt.dto.UserRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = BaseMapper.class)
public interface UserMapper {

    @Mapping(target = "role", source = "user.role.name")
    UserDto convertToDto(User user);

    User convertToDomain(UserRegistrationDto userRegistrationDto);
}
