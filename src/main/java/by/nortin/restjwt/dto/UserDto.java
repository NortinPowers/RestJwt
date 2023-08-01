package by.nortin.restjwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Entity of User")
public class UserDto extends BaseDto {

    @Schema(description = "Username", example = "David")
    private String userName;
    @Schema(description = "User`s role", example = "ROLE_USER")
    private String role;
}
