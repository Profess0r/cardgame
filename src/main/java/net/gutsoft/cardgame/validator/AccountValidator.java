package net.gutsoft.cardgame.validator;

import net.gutsoft.cardgame.entity.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidator {

    public static Map<String, String> validate(Account account) {
        Map<String, String> errorMap = new HashMap<>();
        validateLogin(account.getLogin(), errorMap);
        validatePassword(account.getPassword(), errorMap);
        validateEmail(account.getEmail(), errorMap);
        return errorMap;
    }

    private static void validateLogin(String login, Map<String, String> errorMap) {
        if (login == null) {
            errorMap.put("login", "login can't be empty");
        } else if (login.length() > 30) {
            errorMap.put("login", "too long login (maximum 30 symbols)");
        }
    }

    private static void validatePassword(String password, Map<String, String> errorMap) {
        if (password == null) {
            errorMap.put("password", "password can't be empty");
        } else if (password.length() < 5) {
            errorMap.put("password", "password must be longer than 5 symbols");
        } else if (password.length() > 30) {
            errorMap.put("password", "too long password (maximum 30 symbols)");
        }
    }

    private static void validateEmail(String email, Map<String, String> errorMap) {
        Pattern p = Pattern.compile(".+@.+\\..{1,4}");
        Matcher m = p.matcher(email);

        if (!m.matches()) {
            errorMap.put("email", "wrong email");
        }
    }
}
