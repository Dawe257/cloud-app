package com.dzhenetl.diplom.security.service;

import com.dzhenetl.diplom.security.domain.JwtAuthentication;
import com.dzhenetl.diplom.security.domain.User;
import com.dzhenetl.diplom.dto.JwtRequest;
import com.dzhenetl.diplom.dto.JwtResponse;
import com.dzhenetl.diplom.exception.AuthException;
import com.dzhenetl.diplom.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("Bad Credentials"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            return new JwtResponse(accessToken);
        } else {
            throw new AuthException("Bad Credentials");
        }
    }

    // TODO логаут не работает
    public void logout(@NonNull String token) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.getAuthentication().setAuthenticated(false);
        SecurityContextHolder.setContext(context);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}