package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.user.EditUserRequest;
import com.univalle.bubackend.DTOs.user.EditUserResponse;
import com.univalle.bubackend.DTOs.user.UserRequest;
import com.univalle.bubackend.DTOs.user.UserResponse;
import com.univalle.bubackend.services.student.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Validated @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponse> searchStudentsByCode(@RequestParam("username") String username) {
        return new ResponseEntity<>(userService.findStudentsByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<EditUserResponse> editUser(@Valid @RequestBody EditUserRequest editUserRequest) {
        return new ResponseEntity<>(userService.editUser(editUserRequest), HttpStatus.OK);
    }

}
