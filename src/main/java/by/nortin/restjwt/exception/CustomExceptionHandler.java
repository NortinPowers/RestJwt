package by.nortin.restjwt.exception;

import static by.nortin.restjwt.utils.ResponseUtils.ACCESS_DENIED_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.BAD_CREDENTIALS_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.getExceptionResponse;

import by.nortin.restjwt.model.BaseResponse;
import by.nortin.restjwt.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
//public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse> handleException(BadCredentialsException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleException(AccessDeniedException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.FORBIDDEN, ACCESS_DENIED_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<BaseResponse> handleException(AuthenticationException exception) {
//        ExceptionResponse response = getExceptionResponse(HttpStatus.FORBIDDEN, ACCESS_DENIED_EXCEPTION_MESSAGE, exception);
//        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
//    }
}
