package by.nortin.restjwt.service.impl;

import by.nortin.restjwt.domain.Book;
import by.nortin.restjwt.dto.BookDto;
import by.nortin.restjwt.exception.BookNotFoundException;
import by.nortin.restjwt.mapper.BookMapper;
import by.nortin.restjwt.repository.BookRepository;
import by.nortin.restjwt.service.BookService;
import by.nortin.restjwt.test.utils.ObjectHandlerUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::convertToDto)
                .toList();
    }

    @Override
    public BookDto getBook(Long id) {
        return bookMapper.convertToDto(bookRepository.getReferenceById(id));
    }

    @Override
    public void addBook(BookDto bookDto) {
        bookRepository.save(bookMapper.convertToDomain(bookDto));
    }

    @Override
    public void updateBook(Long id, BookDto bookDto) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            Book updatedBook = bookMapper.convertToDomain(bookDto);
            BeanUtils.copyProperties(updatedBook, book, ObjectHandlerUtils.getIgnoreProperties(updatedBook, "id"));
            bookRepository.save(book);
        } else {
            throw new BookNotFoundException();
        }
    }

    @Override
    public void deleteBook(Long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException();
        }
    }
}
