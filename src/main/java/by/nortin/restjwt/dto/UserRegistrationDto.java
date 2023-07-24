//package by.nortin.restjwt.dto;
//
//import static by.nortin.restjwt.utils.Constants.PASSWORD_PATTERN;
//import static by.nortin.restjwt.utils.Constants.USERNAME_PATTERN;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class UserRegistrationDto {
//
//    @NotBlank(message = "Enter username")
//    @Pattern(regexp = USERNAME_PATTERN, message = "Incorrect username")
//    private String userName;
//    @NotBlank(message = "Enter password")
//    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect password")
//    private String password;
//    @NotBlank(message = "Verify password")
//    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect verify password")
//    private String verifyPassword;
//}
