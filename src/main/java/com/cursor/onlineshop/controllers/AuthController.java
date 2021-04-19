package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CreateAccountDto;
import com.cursor.onlineshop.entities.user.User;
import com.cursor.onlineshop.security.JwtUtils;
import com.cursor.onlineshop.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    // empty line
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtTokenUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest auth) throws AccessDeniedException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        auth.getUsername(), auth.getPassword()));
        final UserDetails userDetails = userService.login(auth.getUsername(), auth.getPassword());
        String jwt = jwtTokenUtil.generateToken(userDetails);
        // try to return token here as part of json string, or as part of header, don't simply put string in the body
        return ResponseEntity.ok(jwt);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> createUserAccount(@RequestBody CreateAccountDto createAccountDto)
            throws AccessDeniedException {
        final UserDetails newAccount = userService.registerUser(createAccountDto);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                createAccountDto.getUsername(),
                createAccountDto.getPassword()
        ));
        String jwt = jwtTokenUtil.generateToken(newAccount);
        // try to return token here as part of json string, or as part of header, don't simply put string in the body
        return ResponseEntity.ok(jwt);
    }

    @PostMapping(value = "/regadmin")
    public ResponseEntity<String> createAdminAccount(@RequestBody CreateAccountDto createAccountDto)
            throws AccessDeniedException {
        final UserDetails newAccount = userService.registerWithRole(createAccountDto);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        createAccountDto.getUsername(), createAccountDto.getPassword()));
        String jwt = jwtTokenUtil.generateToken(newAccount);
        // try to return token here as part of json string, or as part of header, don't simply put string in the body
        return ResponseEntity.ok(jwt);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationRequest {
        private String username;
        private String password;
    }
}