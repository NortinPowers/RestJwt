package by.nortin.restjwt.service;

import by.nortin.restjwt.dto.BookDto;
import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();

    BookDto getBook(Long id);

    void addBook(BookDto bookDto);

    void updateBook(Long id, BookDto bookDto);

    void deleteBook(Long id);
}
