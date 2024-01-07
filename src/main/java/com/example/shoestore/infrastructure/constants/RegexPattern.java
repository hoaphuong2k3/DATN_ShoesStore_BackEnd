package com.example.shoestore.infrastructure.constants;

public class RegexPattern {
    public static final String DATE_FORMAT_REGEX = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1])$";
    public static final String REGEX_PHONENUMBER = "0\\d{9,11}";
    public static final String REGEX_EMAIL = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
    public static final String REGEX_UPPERCASE_CHARACTER = ".*[A-Z].*";
    public static final String REGEX_LOWERCASE_CHARACTER = ".*[a-z].*";
    public static final String REGEX_SPECIAL_CHARACTER = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";
    public static final String REGEX_NUMBER = ".*\\d.*";


}
