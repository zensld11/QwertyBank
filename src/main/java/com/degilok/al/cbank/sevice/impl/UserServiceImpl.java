package com.degilok.al.cbank.sevice.impl;

import com.degilok.al.cbank.entity.User;
import com.degilok.al.cbank.entity.dto.UserDto;
import com.degilok.al.cbank.exception.InvalidEmailException;
import com.degilok.al.cbank.exception.InvalidPasswordException;
import com.degilok.al.cbank.exception.UserNameAlreadyExistException;
import com.degilok.al.cbank.repository.UserRepository;
import com.degilok.al.cbank.security.AuthoritiesNameProvider;
import com.degilok.al.cbank.sevice.UserService;
import com.degilok.al.cbank.validator.EmailValidator;
import com.degilok.al.cbank.validator.PasswordValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final AuthoritiesNameProvider authoritiesNameProvider;
    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(AuthoritiesNameProvider authoritiesNameProvider, PasswordValidator passwordValidator, EmailValidator emailValidator, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authoritiesNameProvider = authoritiesNameProvider;
        this.passwordValidator = passwordValidator;
        this.emailValidator = emailValidator;
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }

    /**
     * при сохранении юзера надо его пароль шифровать(используем BCrypt, он определен в securityConfig как Bean)
     */
    @Override//Если это создание пользователя в системе админом → createUser
    public User createUser(UserDto userDto) {
        if (userRepository.existsUserByName(userDto.getName())) {
            throw new UserNameAlreadyExistException("User Name already exists");
        } else {
            User user = new User();
            if (userDto.getRole() == null) {
                userDto.setRole(authoritiesNameProvider.roleUser());
            }
            if (userDto.getEmail() == null || !emailValidator.isCorrect(userDto.getEmail())) {
                throw new InvalidEmailException("Неправильные данные почты");
            }
            if (userDto.getPassword() == null || !emailValidator.isCorrect(userDto.getEmail())) {
                throw new InvalidPasswordException("Пароль не соответствует требованиям");
            }
            String encryptPassword = passwordEncoder.encode(userDto.getPassword());

            user.setPassword(encryptPassword);
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());
            user.setCreatedAt(LocalDateTime.now());
            log.info("Пароль сохранен {}", encryptPassword);
            log.info("User {}, Email {}, Password {}, Role {}, Created Date {}", user.getName(), user.getEmail(), encryptPassword, user.getRole(), user.getCreatedAt());
            return userRepository.save(user);
        }
    }

    @Override
    public User updateUser(UserDto userDto) {
        try {
            User user = userRepository.findByName(userDto.getName());
            if (user == null) {
                log.info("По такому имени никто не найден", userDto.getName());
                throw new UsernameNotFoundException("Пользователь не найден");
            } else {
                user.setName(userDto.getName());
                if (userDto.getEmail() == null || !emailValidator.isCorrect(userDto.getEmail())) {
                    throw new IllegalArgumentException("Неправильные данные почты");
                } else {
                    user.setEmail(userDto.getEmail());//TODO: думаю надо будет при сохранении пароля проверять на их на валидность
                }
                if (passwordValidator.isValid(userDto.getPassword())) {
                    String encryptPassword = passwordEncoder.encode(userDto.getPassword());
                    user.setPassword(encryptPassword);
                    log.info("Пароль сохранен");
                }
                user.setRole(userDto.getRole());
                user.setCreatedAt(user.getCreatedAt());//ошол эле болгон датаны калтырып койойун
                user.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(user);
            }
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
        }
        return null;
    }

    /**
     * по правилам хорошего тона нельзя отдавать entity из контроллера, необходимо переложить данные в класс DTO и отдавать уже его
     */

    @Override//Если это регистрация пользователя (через веб-сайт, приложение) → registerUser
    public User registerUser(User user) throws Exception {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Такой email уже используется!");
        } else {
            user.setEmail(user.getEmail());
        }
        if (passwordValidator.isValid(user.getPassword())) {
            String encryptPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptPassword);
            log.info("User registered");
        } else {
            throw new IllegalArgumentException("Пароль не совпадает нужному шаблону");
        }
        user.setName(user.getName());
        user.setRole(authoritiesNameProvider.roleUser());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByName(username);
        return user;
    }
}