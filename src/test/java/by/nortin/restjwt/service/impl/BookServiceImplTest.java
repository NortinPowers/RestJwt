package by.nortin.restjwt.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import by.nortin.restjwt.domain.Book;
import by.nortin.restjwt.dto.BookDto;
import by.nortin.restjwt.exception.BookNotFoundException;
import by.nortin.restjwt.mapper.BookMapper;
import by.nortin.restjwt.repository.BookRepository;
import by.nortin.restjwt.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookMapper bookMapper;

    private final Book book;
    private final BookDto bookDto;

    {
        Long id = 1L;
        String author = "Howard Lovecraft";
        String title = "At the Mountains of Madness";
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
    void test_getAllBooks_getBooksList() {
        List<Book> books = List.of(book);
        List<BookDto> bookDtos = List.of(bookDto);

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.convertToDto(book)).thenReturn(bookDto);

        List<BookDto> allBooks = bookService.getAllBooks();

        assertEquals(bookDtos, allBooks);
        verify(bookRepository, atLeastOnce()).findAll();
        verify(bookMapper, atLeastOnce()).convertToDto(book);
    }

    @Test
    void test_addBook_success() {
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.convertToDomain(bookDto)).thenReturn(book);

        bookService.addBook(bookDto);

        verify(bookRepository, atLeastOnce()).save(book);
        verify(bookMapper, atLeastOnce()).convertToDomain(bookDto);
    }

    @Nested
    class TestGetBook {

        @Test
        void test_getBook_isPresent() {
            when(bookRepository.getReferenceById(any())).thenReturn(book);
            when(bookMapper.convertToDto(book)).thenReturn(bookDto);

            BookDto foundBook = bookService.getBook(any());

            assertEquals(foundBook, bookDto);
            verify(bookRepository, atLeastOnce()).getReferenceById(any());
            verify(bookMapper, atLeastOnce()).convertToDto(book);
        }

        @Test
        void test_getBook_entityNotFound() {
            EntityNotFoundException exception = new EntityNotFoundException("not matter");

            when(bookRepository.getReferenceById(any())).thenThrow(exception);

            assertThrows(EntityNotFoundException.class, () -> bookService.getBook(any()));
            verify(bookRepository, atLeastOnce()).getReferenceById(any());
            verify(bookMapper, never()).convertToDto(any());
        }

    }

    @Nested
    class TestUpdateBook {

        @Test
        void test_updateBook_isPresent_success() {
            String title = "New Title";
            String author = "Jack London";
            bookDto.setTitle(title);
            bookDto.setAuthor(author);
            Book updatedBook = new Book();
            Long id = 2L;
            updatedBook.setId(id);
            updatedBook.setTitle(title);
            updatedBook.setAuthor(author);
            Optional<Book> optionalBook = Optional.of(book);

            when(bookRepository.findById(any())).thenReturn(optionalBook);
            when(bookMapper.convertToDomain(bookDto)).thenReturn(updatedBook);
            when(bookRepository.save(book)).thenReturn(book);

            bookService.updateBook(book.getId(), bookDto);

            assertEquals(book.getAuthor(), updatedBook.getAuthor());
            assertEquals(book.getTitle(), updatedBook.getTitle());
            assertNotEquals(book.getId(), id);
            assertNotNull(book.getId());
            verify(bookRepository, atLeastOnce()).findById(any());
            verify(bookMapper, atLeastOnce()).convertToDomain(bookDto);
            verify(bookRepository, atLeastOnce()).save(book);
        }

        @Test
        void test_updateBook_bookNotFound() {
            Optional<Book> empty = Optional.empty();

            when(bookRepository.findById(any())).thenReturn(empty);

            assertThrows(BookNotFoundException.class, () -> bookService.updateBook(any(), bookDto));
            verify(bookRepository, atLeastOnce()).findById(any());
            verifyNoInteractions(bookMapper);
            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    class TestDeleteBook {

        @Test
        void test_deleteBook_success() {
            Optional<Book> optionalBook = Optional.of(book);
            Long id = book.getId();

            when(bookRepository.findById(id)).thenReturn(optionalBook);
            doNothing().when(bookRepository).deleteById(id);

            bookService.deleteBook(id);

            verify(bookRepository, atLeastOnce()).findById(id);
            verify(bookRepository, atLeastOnce()).deleteById(id);
        }

        @Test
        void test_deleteBook_bookNotFound() {
            Optional<Book> empty = Optional.empty();

            when(bookRepository.findById(any())).thenReturn(empty);

            assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(any()));
            verify(bookRepository, atLeastOnce()).findById(any());
            verify(bookRepository, never()).deleteById(any());
        }
    }
}
