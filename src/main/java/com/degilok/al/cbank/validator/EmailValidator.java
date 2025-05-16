package com.degilok.al.cbank.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {

    @Value("false")
    private boolean correctEmail;

    public boolean isCorrect(String email) {
        if (email == null) {
            return true;
        }

        String errorMessage;
        Pattern emailPattern;

        if (correctEmail) {
            emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
            errorMessage = "Регистрация доступна только с домена @gmail.com";
        } else {
            emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
            errorMessage = "Электронная почта должна быть корректной";
        }
        boolean isEmailValid = emailPattern.matcher(email).matches();

        if (!isEmailValid) {
            errorMessage = "Ошибка! Данные почты некорректны";
            return false;
        }
        return true;
    }
}
