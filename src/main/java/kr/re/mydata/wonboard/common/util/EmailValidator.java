package kr.re.mydata.wonboard.common.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EmailValidator {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}