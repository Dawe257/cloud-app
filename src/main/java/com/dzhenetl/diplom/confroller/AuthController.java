package com.dzhenetl.diplom.confroller;

import com.dzhenetl.diplom.dto.JwtRequest;
import com.dzhenetl.diplom.dto.JwtResponse;
import com.dzhenetl.diplom.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";

    private final AuthService authService;

    @PostMapping(LOGIN_URL)
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping(LOGOUT_URL)
    public ResponseEntity logout(@RequestHeader(name = "auth-token") String authToken) {
        authService.logout(authToken);
        return ResponseEntity.status(200).build();
    }
}