package by.nortin.restjwt.utils;

import by.nortin.restjwt.model.ExceptionResponse;
import by.nortin.restjwt.model.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@UtilityClass
public class ResponseUtils {

    public static final String DATE_PATTERN = "dd-MM-yyyy";
    public static final String CREATION_MESSAGE = "The %s have been successful created";
    public static final String UPDATE_MESSAGE = "The %s have been successful updated";
    public static final String DELETION_MESSAGE = "The %s have been successful deleted";
    public static final String BAD_CREDENTIALS_EXCEPTION_MESSAGE = "Incorrect login or password";
    public static final String BAD_TOKEN_EXCEPTION_MESSAGE = "Incorrect token";
    public static final String ACCESS_DENIED_EXCEPTION_MESSAGE = "You don't have access rights";
    public static final String NOT_FOUND_EXCEPTION_MESSAGE = "Specify the entered data";
    public static final String BOOK_NOT_FOUND_EXCEPTION_MESSAGE = "The entered book is not listed in the database";
    public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE = "The input data does not correspond to the required";
    public static final String JPA_OBJECT_RETRIEVAL_FAILURE_EXCEPTION_MESSAGE = "The data entered violates the established requirements";
    public static final String HTTP_NOT_READABLE_EXCEPTION_MESSAGE = "The entered data is incorrect and leads to an error";
    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE = "The transmitted data did not pass verification";

    public static <T> MessageResponse getSuccessResponse(String message, T t) {
        return new MessageResponse(HttpStatus.OK.value(), String.format(message, getClassName(t).toLowerCase()), t);
    }

    public static ExceptionResponse getExceptionResponse(HttpStatus status, String message, Exception exception) {
        return new ExceptionResponse(status.value(), message, exception.getClass().getSimpleName());
    }

    public static List<String> getErrorValidationMessages(MethodArgumentNotValidException exception) {
        return exception.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

    public static ObjectMapper getObjectMapperWithTimeModule() {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        mapper.setDateFormat(dateFormat);
        return mapper;
    }

    private <T> String getClassName(T t) {
        String className = t.getClass().getSimpleName();
        return className.substring(0, className.length() - 3);
    }
}
