package by.nortin.restjwt.utils;

import static by.nortin.restjwt.utils.ResponseUtils.getErrorValidationMessages;
import static by.nortin.restjwt.utils.ResponseUtils.getExceptionResponse;
import static by.nortin.restjwt.utils.ResponseUtils.getSuccessResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import by.nortin.restjwt.model.ExceptionResponse;
import by.nortin.restjwt.model.MessageResponse;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class ResponseUtilsTest {

    @Test
    void test_getExceptionResponse_correctStatus() {
        String message = "error response message";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Exception exception = new Exception();
        ExceptionResponse expectedResponse = new ExceptionResponse(status, message, exception.getClass().getSimpleName());

        ExceptionResponse response = getExceptionResponse(status, message, exception);

        assertEquals(expectedResponse.getStatus(), response.getStatus());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(expectedResponse.getType(), response.getType());
    }

    @Test
    void test_getErrorValidationMessages_getErrorsList() {
        String firstError = "First error";
        String secondError = "Second error";
        List<String> expectedList = List.of("First error", "Second error");
        List<ObjectError> errors = List.of(
                new FieldError("objectName", "field1", firstError),
                new FieldError("objectName", "field2", secondError)
        );
        MethodArgumentNotValidException exception = Mockito.mock(MethodArgumentNotValidException.class);

        when(exception.getAllErrors()).thenReturn(errors);

        List<String> errorValidationMessages = getErrorValidationMessages(exception);

        assertEquals(2, errorValidationMessages.size());
        assertEquals(expectedList, errorValidationMessages);
    }

    /*
     public static ObjectMapper getObjectMapperWithTimeModule() {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        mapper.setDateFormat(dateFormat);
        return mapper;
    }
     */
    @Test
    void test_getObjectMapperWithTimeModule() {
    }

    @Nested
    class TestGetSuccessResponse {

        private final String message;
        private final String expectedMessage;
        private final String className;
        private int status;

        {
            status = 200;
            message = "message with embedded variable - '%s'";
            className = "testClassName";
            expectedMessage = String.format(message, className.toLowerCase());
        }

        @Test
        void test_getSuccessResponse_correctStatus() {
            MessageResponse response = new MessageResponse(status, message, className);

            MessageResponse successResponse = getSuccessResponse(message, className);

            assertEquals(response.getStatus(), successResponse.getStatus());
            assertEquals(expectedMessage, successResponse.getMessage());
        }

        @Test
        void test_getSuccessResponse_incorrectStatus() {
            status = 300;
            MessageResponse response = new MessageResponse(status, message, className);

            MessageResponse successResponse = getSuccessResponse(message, className);

            assertNotEquals(response.getStatus(), successResponse.getStatus());
        }
    }
}
