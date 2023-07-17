package by.nortin.restjwt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorValidationResponse extends BaseResponse {

    @Schema(description = "List of validation errors", type = "array", example = "[\"The 'name' field is required\", \"Some validation error\"]")
    private List<String> errors;
    @Schema(description = "Exception type", example = "Some validation exception")
    private String type;

    public ErrorValidationResponse(Integer status, List<String> errors, String type) {
        super(status);
        this.errors = errors;
        this.type = type;
    }
}
