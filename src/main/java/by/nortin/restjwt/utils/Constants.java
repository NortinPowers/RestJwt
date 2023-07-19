package by.nortin.restjwt.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String ROLES = "roles";
    public static final String USERNAME_PATTERN = "[a-zA-Z0-9]{3,30}";
    public static final String PASSWORD_PATTERN = "[a-zA-Z0-9]{4,30}";
    public static final String AUTHOR_PATTERN = "[A-Z]+([a-zA-Z-`]+)*+\\s+[A-Z]+([a-zA-Z]+)*";
}
