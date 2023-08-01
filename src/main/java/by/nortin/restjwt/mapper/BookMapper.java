package by.nortin.restjwt.mapper;

import by.nortin.restjwt.domain.Book;
import by.nortin.restjwt.dto.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = BaseMapper.class)
public interface BookMapper {

    BookDto convertToDto(Book book);

    Book convertToDomain(BookDto bookDto);
}
