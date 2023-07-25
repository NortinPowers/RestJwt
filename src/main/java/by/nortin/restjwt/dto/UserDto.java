package by.nortin.restjwt.dto;

import by.nortin.restjwt.validator.PasswordMatching;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatching
//@Valid
@Schema(description = "Entity of User")
public class UserDto extends BaseDto {

    //    @NotBlank(message = "Enter username")
//    @Pattern(regexp = USERNAME_PATTERN, message = "Incorrect username")
//    @UserExist
    @Schema(description = "Username", example = "David")
    private String userName;
    //    @NotBlank(message = "Enter password")
//    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect password")
//    @Schema(description = "User`s password", example = "qwerty")
//    private String password;
//    @NotBlank(message = "Verify password")
//    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect verify password")
//    @Schema(description = "User`s verify password", example = "qwerty")
//    private String verifyPassword;
    @Schema(description = "User`s role", example = "ROLE_USER")
    private String role;
}
