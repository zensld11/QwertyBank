package com.degilok.al.cbank.config;


import com.degilok.al.cbank.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

//https://acte.ltd/utils/openssl генератор ключей
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    RSAPublicKey publicKey; //public key для верификации подписи токена

    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey; //private key для его подписи

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/user/check").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/reset-password").authenticated()
                        .anyRequest().authenticated()).sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedPage("/doesntHasAccess")
                );
//                .cors(cors -> cors//TODO:механизм, позволяющий получить доступ к ресурсам, которые находятся на другом источнике(сложные cors сначала проверяет, а потом решает блокировать или разрешать их)
//                        .configurationSource(request -> {
//                            CorsConfiguration corsConfig = new CorsConfiguration();
//                            corsConfig.setAllowedOrigins(List.of("http://localhost:9092"));//разрешенные источники
//                            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));//разрешенные методы
//                            corsConfig.setAllowedHeaders(List.of("*"));//разрешенные заголовки
//                            corsConfig.setAllowCredentials(true);//нужно ли передавать куки
//                            corsConfig.setMaxAge(3600L);
//                            return corsConfig;
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }


    @Bean
//объявление бин компонент passwordEncoder, который мы будем использовать при создании новых пользователей и при аутентификации
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
/**
 * BCryptPasswordEncoder – применяет надежное шифрование bcrypt;
 * .
 * NoOpPasswordEncoder – не применяет шифрования;
 * .
 * Pbkdf2PasswordEncoder – применяет шифрование PBKDF2;
 * .
 * SCryptPasswordEncoder – применяет шифрование Scrypt;
 * .
 * StandardPasswordEncoder – применяет шифрование SHA-256.
 */