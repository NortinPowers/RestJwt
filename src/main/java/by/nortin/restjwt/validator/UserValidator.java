//package by.nortin.restjwt.validator;
//
//import static by.nortin.restjwt.utils.Constants.PASSWORDS_MATCHING;
//
//import by.nortin.restjwt.dto.UserDto;
//import by.nortin.restjwt.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//@Component
//@RequiredArgsConstructor
//public class UserValidator implements Validator {
//
//    private final UserService userService;
//
//    @Override
////    public boolean supports(Class<?> clazz) {
////        return UserRegistrationDto.class.equals(clazz);
////    }
//    public boolean supports(Class<?> clazz) {
//        return UserDto.class.equals(clazz);
//    }
//
//    @Override
//    public void validate(Object target, Errors errors) {
//        UserDto user = (UserDto) target;
//        checkExistUser(errors, user);
//        checkPasswordInputVerify(errors, user);
//    }
//
//    private void checkExistUser(Errors errors, UserDto user) {
////        if (userService.isUserExist(user)) {
////            errors.rejectValue("userName", "userName.invalid", "User with this username already exist");
////        }
//
//    }
//
//    private void checkPasswordInputVerify(Errors errors, UserDto user) {
//        if (!user.getPassword().equals(user.getVerifyPassword())) {
//            errors.rejectValue("verifyPassword", "verifyPassword.invalid", PASSWORDS_MATCHING);
//        }
//    }
//}
