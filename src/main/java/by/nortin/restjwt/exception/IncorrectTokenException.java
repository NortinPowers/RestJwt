package by.nortin.restjwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class IncorrectTokenException extends RuntimeException {

    private final String message;
}
