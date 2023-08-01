package by.nortin.restjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.nortin.restjwt.domain.Book;
import by.nortin.restjwt.dto.BookDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    private final Book book;
    private final BookDto bookDto;

    {
        Long id = 1L;
        String author = "Test Author";
        String title = "Some Title";
        book = new Book();
        book.setId(id);
        book.setAuthor(author);
        book.setTitle(title);
        bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setAuthor(author);
        bookDto.setTitle(title);
    }

    @Test
    void test_convertToDto() {
        assertEquals(bookDto, bookMapper.convertToDto(book));
    }

    @Test
    void test_convertToDomain() {
        assertEquals(book, bookMapper.convertToDomain(bookDto));
    }
}
