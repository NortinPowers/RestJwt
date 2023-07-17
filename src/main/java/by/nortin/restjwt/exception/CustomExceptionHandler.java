package by.nortin.restjwt.exception;

import static by.nortin.restjwt.utils.ResponseUtils.BAD_CREDENTIALS_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.NOT_FOUND_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.utils.ResponseUtils.getExceptionResponse;

import by.nortin.restjwt.model.BaseResponse;
import by.nortin.restjwt.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse> handleException(BadCredentialsException exception) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS_EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
