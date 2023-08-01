package by.nortin.restjwt.controller;

import static by.nortin.restjwt.utils.ResponseUtils.BOOK_NOT_FOUND_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.CREATION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.DELETION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.HTTP_NOT_READABLE_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.NOT_FOUND_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.UPDATE_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.getExceptionResponse;
import static by.nortin.restjwt.utils.ResponseUtils.getObjectMapperWithTimeModule;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.nortin.restjwt.dto.BookDto;
import by.nortin.restjwt.exception.BookNotFoundException;
import by.nortin.restjwt.model.ErrorValidationResponse;
import by.nortin.restjwt.model.ExceptionResponse;
import by.nortin.restjwt.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private final ObjectMapper mapper;

    {
        mapper = getObjectMapperWithTimeModule();
    }

    @Nested
    class TestGetAll {

        private final String url = "/book";

        @Test
        @WithAnonymousUser
        void test_getAll_anonymous_denied() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access, you need to transfer a token."));
        }

        @Test
        @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
        void test_getAll_authorized_alloyed() throws Exception {
            BookDto firstBook = new BookDto();
            firstBook.setId(1L);
            firstBook.setTitle("First book");
            firstBook.setAuthor("Author of first book");
            BookDto secondBook = new BookDto();
            secondBook.setId(2L);
            secondBook.setTitle("Second book");
            secondBook.setAuthor("Author of second book");
            List<BookDto> books = List.of(firstBook, secondBook);

            when(bookService.getAllBooks()).thenReturn(books);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(books)));
        }

        @Test
        @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
        void test_getAll_authorized_bDError() throws Exception {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("not matter");
            ExceptionResponse response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE, exception);

            doThrow(exception).when(bookService).getAllBooks();

            mockMvc.perform(get(url))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }

    @Nested
    class TestGetOne {

        private final String url = "/book/{id}";
        private final Long id;

        {
            id = 1L;
        }

        @Test
        @WithAnonymousUser
        void test_getOne_anonymous_denied() throws Exception {
            mockMvc.perform(get(url, id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access, you need to transfer a token."));
        }

        @Test
        @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
        void test_getOne_authorized_alloyed() throws Exception {
            BookDto bookDto = new BookDto();
            bookDto.setId(id);
            bookDto.setTitle("Test book");
            bookDto.setAuthor("Some author");

            when(bookService.getBook(id)).thenReturn(bookDto);

            mockMvc.perform(get(url, id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(bookDto)));
        }

        @Test
        @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
        void test_getOne_authorized_entityNotFound() throws Exception {
            EntityNotFoundException exception = new EntityNotFoundException();
            ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_FOUND, NOT_FOUND_EXCEPTION_MESSAGE, exception);

            doThrow(exception).when(bookService).getBook(any());

            mockMvc.perform(get(url, id))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }

    @Nested
    class TestCreate {

        private final String url = "/book";
        private final BookDto bookDto;

        {
            bookDto = new BookDto();
            bookDto.setTitle("New Book");
            bookDto.setAuthor("Book`s Author");
        }

        @Test
        @WithAnonymousUser
        void test_create_anonymous_denied() throws Exception {
            mockMvc.perform(post(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access, you need to transfer a token."));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void test_create_roleUser_denied() throws Exception {
            AccessDeniedException exception = new AccessDeniedException("Access Denied");
            ExceptionResponse response = getExceptionResponse(HttpStatus.FORBIDDEN, exception.getMessage(), exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookDto)))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_create_roleAdmin_alloyed() throws Exception {
            doNothing().when(bookService).addBook(bookDto);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(CREATION_MESSAGE, "book")));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_create_roleAdmin_invalidBody() throws Exception {
            bookDto.setAuthor("Invalid author`s name");
            List<String> errors = List.of("Incorrect author`s name");
            ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            doNothing().when(bookService).addBook(bookDto);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_create_roleAdmin_emptyBody() throws Exception {
            bookDto.setAuthor("");
            bookDto.setTitle("");
            List<String> errors = List.of("Enter title", "Incorrect author`s name", "Enter author");
            ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, errors, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            doNothing().when(bookService).addBook(bookDto);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_create_roleAdmin_incorrectBody() throws Exception {
            String body = "book, author";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("not matter", new MockHttpInputMessage("not matter".getBytes()));
            ExceptionResponse response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, HTTP_NOT_READABLE_EXCEPTION_MESSAGE, exception);

            doNothing().when(bookService).addBook(bookDto);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(body)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }

    @Nested
    class TestUpdate {

        private final String url;
        private final Long id;
        private final BookDto bookDto;

        {
            url = "/book/{id}";
            id = 1L;
            bookDto = new BookDto();
            bookDto.setTitle("Updated Title");
            bookDto.setAuthor("Updated Author");
        }

        @Test
        @WithAnonymousUser
        void test_update_anonymous_denied() throws Exception {
            mockMvc.perform(patch(url, id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access, you need to transfer a token."));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void test_update_roleUser_denied() throws Exception {
            AccessDeniedException exception = new AccessDeniedException("Access Denied");
            ExceptionResponse response = getExceptionResponse(HttpStatus.FORBIDDEN, exception.getMessage(), exception);

            mockMvc.perform(patch(url, id)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookDto)))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_update_roleAdmin_allayed() throws Exception {
            doNothing().when(bookService).updateBook(id, bookDto);

            mockMvc.perform(patch(url, id)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(UPDATE_MESSAGE, "book")));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_update_roleAdmin_bookNotFound() throws Exception {
            BookNotFoundException exception = new BookNotFoundException();
            ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_EXCEPTION_MESSAGE, exception);

            doThrow(exception).when(bookService).updateBook(id, bookDto);

            mockMvc.perform(patch(url, id)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_update_roleAdmin_invalidBody() throws Exception {
            bookDto.setAuthor("");
            List<String> errors = List.of("Enter author", "Incorrect author`s name");
            ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            doNothing().when(bookService).updateBook(id, bookDto);

            mockMvc.perform(patch(url, id)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_update_roleAdmin_incorrectBody() throws Exception {
            String body = "author, title";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                    "not matter",
                    new MockHttpInputMessage("not matter".getBytes())
            );
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HTTP_NOT_READABLE_EXCEPTION_MESSAGE,
                    exception
            );

            doNothing().when(bookService).updateBook(id, bookDto);

            mockMvc.perform(patch(url, id)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(body)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }

    @Nested
    class TestDelete {

        private final String url;
        private final Long id;

        {
            url = "/book/{id}";
            id = 1L;
        }

        @Test
        @WithAnonymousUser
        void test_delete_anonymous_denied() throws Exception {
            mockMvc.perform(delete(url, id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access, you need to transfer a token."));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void test_delete_roleUser_denied() throws Exception {
            AccessDeniedException exception = new AccessDeniedException("Access Denied");
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.FORBIDDEN,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(delete(url, id))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_delete_roleAdmin_alloyed() throws Exception {

            mockMvc.perform(delete(url, id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(DELETION_MESSAGE, "book")));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void test_delete_roleAdmin_bookNotFound() throws Exception {
            BookNotFoundException exception = new BookNotFoundException();
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    BOOK_NOT_FOUND_EXCEPTION_MESSAGE,
                    exception
            );

            doThrow(exception).when(bookService).deleteBook(id);

            mockMvc.perform(delete(url, id))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }
    }
}
