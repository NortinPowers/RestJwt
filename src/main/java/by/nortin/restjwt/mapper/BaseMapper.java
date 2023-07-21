package by.nortin.restjwt.mapper;

import by.nortin.restjwt.domain.BaseEntity;
import by.nortin.restjwt.dto.BaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseMapper {

    BaseDto convertToDto(BaseEntity baseEntity);

    BaseEntity convertToEntity(BaseDto baseDto);
}
