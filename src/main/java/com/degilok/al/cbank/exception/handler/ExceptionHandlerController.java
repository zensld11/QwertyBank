package com.degilok.al.cbank.exception.handler;

import com.degilok.al.cbank.exception.InvalidEmailException;
import com.degilok.al.cbank.exception.InvalidPasswordException;
import com.degilok.al.cbank.exception.UserNameAlreadyExistException;
import com.degilok.al.cbank.exception.UserNameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = InvalidEmailException.class)
    public ResponseEntity<String> handleInvalidEmailException(InvalidEmailException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = UserNameAlreadyExistException.class)
    public ResponseEntity<String> handleUserNameAlreadyException(UserNameAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = UserNameNotFoundException.class)
    public ResponseEntity<String> handleUserNameNotFoundException(UserNameNotFoundException e) {
        return ResponseEntity.ofNullable("Имя пользователя не найдено или его не существует " + e.getMessage());
    }
}