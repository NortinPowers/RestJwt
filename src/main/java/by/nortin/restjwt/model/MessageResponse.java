package by.nortin.restjwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse extends BaseResponse {

    @Schema(description = "A message describing the completed request", example = "Some message")
    private String message;
    @JsonIgnore
    private Object object;

    public MessageResponse(Integer status, String message, Object object) {
        super(status);
        this.message = message;
        this.object = object;
    }
}
