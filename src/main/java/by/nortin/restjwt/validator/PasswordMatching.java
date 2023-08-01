package by.nortin.restjwt.validator;

import static by.nortin.restjwt.test.utils.Constants.PASSWORDS_MATCHING;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordMatchingValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface PasswordMatching {

    String message() default PASSWORDS_MATCHING;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
