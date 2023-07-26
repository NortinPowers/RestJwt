package by.nortin.restjwt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ExceptionResponse extends BaseResponse {

    @Schema(description = "Message describing the exception", example = "Some message")
    private String message;
    @Schema(description = "Exception type", example = "Some exception type")
    private String type;

    public ExceptionResponse(HttpStatus status, String message, String type) {
        super(status.value());
        this.message = message;
        this.type = type;
    }
}
