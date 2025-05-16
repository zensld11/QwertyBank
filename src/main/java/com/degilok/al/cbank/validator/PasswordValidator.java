package com.degilok.al.cbank.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    @Value("${security.auth.password.min.length}")
    private int passwordMinLength;

    @Value("${security.auth.password.min.letters-count}")
    private int passwordMinLettersCount;

    @Value("${security.auth.password.min.upper-letters-count}")
    private int passwordMinUpperLettersCount;


    public boolean isValid(String password) {
        if (password == null) return true;

        boolean passwordValid = true;
        int lettersCount = 0, upperLettersCount = 0;
        StringBuilder errorMessage = new StringBuilder();

        if (password.length() < passwordMinLength) {
            errorMessage.append(String.format("Пароль должен быть как минимум %s символов в длину. ", passwordMinLength));
            passwordValid = false;
        }

        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i)))
                lettersCount++;

            if (Character.isUpperCase(password.charAt(i)))
                upperLettersCount++;
        }

        if (lettersCount < passwordMinLettersCount) {
            errorMessage.append(String.format("Пароль должен содержать как минимум %s строчных букв. ", passwordMinLettersCount));
            passwordValid = false;
        }

        if (upperLettersCount < passwordMinUpperLettersCount) {
            errorMessage.append(String.format("Пароль должен содержать как минимум %s заглавных букв. ", passwordMinUpperLettersCount));
            passwordValid = false;
        }

        if (!passwordValid) {
            return false;
        }
        return true;
    }
}