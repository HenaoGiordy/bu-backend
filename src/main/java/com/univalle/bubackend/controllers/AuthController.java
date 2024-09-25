package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.auth.*;
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

    @PostMapping("/verify-token")
    public ResponseEntity<VerifyResponse> verifyToken(@RequestBody VerifyRequest verifyRequest) {
        return new ResponseEntity<>(userDetailsService.verifyToken(verifyRequest), HttpStatus.OK);
    }

    @PostMapping("/email-reset")
    public ResponseEntity<SendResetPasswordResponse> emailResetPassword(@RequestBody SendResetPasswordRequest sendResetPasswordRequest) {
        return new ResponseEntity<>(userDetailsService.sendResetPassword(sendResetPasswordRequest), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return new ResponseEntity<>(userDetailsService.resetPassword(resetPasswordRequest), HttpStatus.OK);
    }

}
