package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.auth.UserRequest;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@Validated @RequestBody UserRequest userRequest) {
        UserEntity user = userService.createUser(userRequest);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchStudentsByCode(@RequestParam("username") String username) {
        try {
            UserEntity student = userService.findStudentsByUsername(username);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
