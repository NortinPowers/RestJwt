package by.nortin.restjwt.exception;

import static by.nortin.restjwt.utils.ResponseUtils.BAD_CREDENTIALS_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.BOOK_NOT_FOUND_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.HTTP_NOT_READABLE_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.JPA_OBJECT_RETRIEVAL_FAILURE_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.NOT_FOUND_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.getErrorValidationMessages;
import static by.nortin.restjwt.utils.ResponseUtils.getExceptionResponse;

import by.nortin.restjwt.model.BaseResponse;
import by.nortin.restjwt.model.ErrorValidationResponse;
import by.nortin.restjwt.model.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class CustomExceptionHandler {
//public class CustomExceptionHandler implements AuthenticationEntryPoint {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse> handleException(BadCredentialsException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

//    -not work
//    @ExceptionHandler(InsufficientAuthenticationException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseEntity<BaseResponse> handleException(InsufficientAuthenticationException exception) {
//        ExceptionResponse response = getExceptionResponse(HttpStatus.UNAUTHORIZED, "InsufficientAuthenticationException", exception);
//        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//    }
////    -not work test
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseEntity<BaseResponse> handleException(RuntimeException exception) {
//        ExceptionResponse response = getExceptionResponse(HttpStatus.UNAUTHORIZED, "RuntimeException", exception);
//        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//    }
//
//    //    -not work test
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseEntity<BaseResponse> handleException(Exception exception) {
//        ExceptionResponse response = getExceptionResponse(HttpStatus.UNAUTHORIZED, "Exception", exception);
//        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//    }

//    @ExceptionHandler(IncorrectTokenException.class)
//    public ResponseEntity<BaseResponse> handleException(IncorrectTokenException exception) {
//        ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_ACCEPTABLE, exception.getMessage(), exception);
//        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleException(AccessDeniedException exception) {
//        ExceptionResponse response = getExceptionResponse(HttpStatus.FORBIDDEN, ACCESS_DENIED_EXCEPTION_MESSAGE, exception);
        ExceptionResponse response = getExceptionResponse(HttpStatus.FORBIDDEN, exception.getMessage(), exception);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

//    -not work
//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ResponseEntity<BaseResponse> handleException(AuthenticationException exception) {
//        ExceptionResponse response = getExceptionResponse(HttpStatus.FORBIDDEN, exception.getMessage(), exception);
//        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
//    }

//    @ExceptionHandler(MalformedJwtException.class)
//    public ResponseEntity<BaseResponse> handleException(MalformedJwtException exception) {
//        ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_ACCEPTABLE, "MalformedJwtException", exception);
//        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
//    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse> handleException(EntityNotFoundException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BaseResponse> handleException(BookNotFoundException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_FOUND, NOT_FOUND_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<BaseResponse> handleException(DataIntegrityViolationException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataSourceLookupFailureException.class)
    private ResponseEntity<BaseResponse> handleException(DataSourceLookupFailureException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    private ResponseEntity<BaseResponse> handleException(JpaObjectRetrievalFailureException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, JPA_OBJECT_RETRIEVAL_FAILURE_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<BaseResponse> handleException(HttpMessageNotReadableException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, HTTP_NOT_READABLE_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<BaseResponse> handleException(MethodArgumentNotValidException exception) {
        ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST, getErrorValidationMessages(exception), METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);
        return new ResponseEntity<>(errorValidationResponse, HttpStatus.BAD_REQUEST);
    }

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
////        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
//
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.getWriter().write("{ \"error\": \"To get access, you need to transfer a token.\" }");
//    }
}
