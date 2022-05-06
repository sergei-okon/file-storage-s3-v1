package ua.com.sergeiokon.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ua.com.sergeiokon.model.LoginCredentials;
import ua.com.sergeiokon.security.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthRestController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> loginHandler(@RequestBody LoginCredentials body) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());
        authenticationManager.authenticate(authInputToken);
        String token = jwtUtil.generateToken(body.getEmail());

        Map<Object, Object> response = new HashMap<>();
        response.put("email", body.getEmail());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            RuntimeException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}