package com.dzhenetl.diplom.security.service;

import com.dzhenetl.diplom.security.domain.JwtAuthentication;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.security.dto.JwtRequest;
import com.dzhenetl.diplom.security.dto.JwtResponse;
import com.dzhenetl.diplom.security.exception.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            return new JwtResponse(accessToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    // TODO логаут не работает
    public void logout(@NonNull String token) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.getAuthentication().setAuthenticated(false);
        SecurityContextHolder.setContext(context);
//        jwtProvider.getAccessClaims(token).
//        jwtProvider.getAccessClaims(token).setExpiration(new Date());
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}