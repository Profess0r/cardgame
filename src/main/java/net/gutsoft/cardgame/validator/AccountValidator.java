package net.gutsoft.cardgame.validator;

import net.gutsoft.cardgame.entity.Account;

import java.util.HashMap;
import java.util.Map;

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
            errorMap.put("login", "login == null");
        } else if (login.length() < 3) {
            errorMap.put("login", "login.length() < 3");
        } else if (login.length() > 10) {
            errorMap.put("login", "login.length() > 10");
        }
    }

    private static void validatePassword(String password, Map<String, String> errorMap) {
        if (password == null) {
            errorMap.put("password", "password == null");
        } else if (password.length() < 3) {
            errorMap.put("password", "password.length() < 3");
        } else if (password.length() > 10) {
            errorMap.put("password", "password.length() > 10");
        }
    }

    private static void validateEmail(String email, Map<String, String> errorMap) {
        if (email == null) {
            errorMap.put("email", "email == null");
        } else if (email.length() < 3) {
            errorMap.put("email", "email.length() < 3");
        } else if (email.length() > 10) {
            errorMap.put("email", "email.length() > 10");
        }
    }
}
