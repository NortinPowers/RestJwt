package by.nortin.restjwt.impl;

import by.nortin.restjwt.domain.Book;
import by.nortin.restjwt.dto.BookDto;
import by.nortin.restjwt.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookMapper bookMapper;

    private final BookDto bookDto;
    private final Book book;

    {
        Long id = 1L;
        String title = "The Dark Tower";
        String author = "Stephen King";
        bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
    }

    @Test
    void testConvert() {
        assertEquals(book, bookMapper.convertToDomain(bookDto));
        System.out.println(bookMapper.convertToDomain(bookDto).getId());
        System.out.println(bookMapper.convertToDomain(bookDto));
        assertEquals(bookDto, bookMapper.convertToDto(book));
        System.out.println(bookMapper.convertToDto(book));
    }
}
