package com.degilok.al.cbank.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

/*
                проверка, что имя юзера было извлечено из токена
                проверка, что в текущем запросе юзер еще не был аутентифицирован
                проверка, что токен еще не истек, если время истечения токена позже текущего времени, значит токен еще действителен

 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtDecoder jwtDecoder;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtDecoder jwtDecoder) {
        this.userDetailsService = userDetailsService;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    //doFilterInternal вызывается для каждого входящего http запроса(основной механизм фильтрации в секьюрити)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");//получение заголовка авторизатион
        if (authHeader != null && authHeader.startsWith("Bearer ")) {//проверка наличия биарер
            String jwtToken = authHeader.substring(7);//удаление префикса биаерер, оставляя только сам токен
            try {
                var jwt = jwtDecoder.decode(jwtToken);//декодирование jwt
                String userName = jwt.getClaim("sub");//извлекает из токена информацию о пользователе(имя пользователя и его идентификатор)

                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null
                        && jwt.getExpiresAt().isAfter(Instant.now())) {
                    //проверка существует ли аутентификация и не истек ли срок действия токена
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName); // loadUserByUsername - принимает имя пользователя и отыскивает соответсвующий объект UserDetails

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

                filterChain.doFilter(request, response);
            } catch (Exception e) {
                handleException(response, "invalidJwt", "Токен не валиден", HttpStatus.UNAUTHORIZED);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    //метод для обработки ошибок
    private void handleException(HttpServletResponse response, String errorCode, String errorMessage, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        String responseBody = mapper.writeValueAsString(new Error(errorCode));
        response.getWriter().write(responseBody);
    }
}
