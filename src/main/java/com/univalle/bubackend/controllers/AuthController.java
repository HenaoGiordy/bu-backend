package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.auth.AuthRequest;
import com.univalle.bubackend.DTOs.auth.AuthResponse;
import com.univalle.bubackend.services.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(userDetailsService.login(authRequest), HttpStatus.OK);
    }

}
