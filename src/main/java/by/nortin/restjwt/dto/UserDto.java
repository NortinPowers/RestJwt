package by.nortin.restjwt.dto;

import static by.nortin.restjwt.utils.Constants.PASSWORD_PATTERN;
import static by.nortin.restjwt.utils.Constants.USERNAME_PATTERN;

import by.nortin.restjwt.validator.PasswordMatching;
import by.nortin.restjwt.validator.UserExist;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatching
@Valid
public class UserDto extends BaseDto {

    @NotBlank(message = "Enter username")
    @Pattern(regexp = USERNAME_PATTERN, message = "Incorrect username")
    @UserExist
    private String userName;
    @NotBlank(message = "Enter password")
    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect password")
    private String password;
    @NotBlank(message = "Verify password")
    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect verify password")
    private String verifyPassword;
//    private RoleDto roleDto;
}
