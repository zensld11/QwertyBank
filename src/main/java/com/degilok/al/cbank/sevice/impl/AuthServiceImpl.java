package com.degilok.al.cbank.sevice.impl;

import com.degilok.al.cbank.security.JwtTokenProvider;
import com.degilok.al.cbank.sevice.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(String username, String password) throws Exception {
        String result;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        log.info("PasswordUserDetails, пароль переданный в параметры {} {}", userDetails.getPassword(), password);
        if (userDetails == null) {
            throw new UsernameNotFoundException("Имя пользователя не найдено или его не существует");
        }
        log.info("Loading user {}", userDetails.getUsername());
        log.info("Password {}", userDetails.getPassword());
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            log.info("Пароль потвержден, токен генерируется");

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            result = jwtTokenProvider.generateToken(authentication);
            return result;
        }
        log.info("Password not validated");
        result = "Invalid Token, Password or UserName";
        return result;
    }
}
