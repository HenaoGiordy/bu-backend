package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.report.DeleteResponse;
import com.univalle.bubackend.DTOs.user.*;
import com.univalle.bubackend.models.RoleName;
import com.univalle.bubackend.services.UserDetailServiceImpl;
import com.univalle.bubackend.services.student.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Security Token")
public class UserController {

    private final UserServiceImpl userService;
    private final UserDetailServiceImpl userDetailServiceImpl;

    public UserController(UserServiceImpl userService, UserDetailServiceImpl userDetailServiceImpl) {
        this.userService = userService;
        this.userDetailServiceImpl = userDetailServiceImpl;
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

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<UserResponse>> importUser(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("role") RoleName roleName) {
        List<UserResponse> users = userService.importUsers(file, roleName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<PasswordResponse> changePassword(@Valid @RequestBody PasswordRequest passwordRequest) {
        return new ResponseEntity<>(userService.changePassword(passwordRequest), HttpStatus.OK);
    }

    @GetMapping("/list")
        public ResponseEntity<Page<ListUser>> getAllUsers(
                @RequestParam(value = "page", defaultValue = "0") int page,
                @RequestParam(value = "size", defaultValue = "10") int size,
                @RequestParam(value = "filter", required = true) String filter) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ListUser> users = userService.listUsers(filter, pageable);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PutMapping("/delete")
    public ResponseEntity<DeleteResponse> removeBeneficiaries() {
        userService.deleteBeneficiaries();
        return new ResponseEntity<>(new DeleteResponse("Beneficiarios eliminados correctamente"), HttpStatus.OK);
    }

    @PutMapping("/delete/{username}")
    public ResponseEntity<DeleteResponse> removeBeneficiary(@PathVariable String username ) {
        userService.deleteBeneficiary(username);
        return new ResponseEntity<>(new DeleteResponse("Beneficiario borrado exitosamente"), HttpStatus.OK);
    }

 
    @GetMapping("/{username}")
    public ResponseEntity<ViewProfileResponse> getUserBenefits(@PathVariable String username) {
        ViewProfileResponse userBenefits = userDetailServiceImpl.getUserDetails(username);
        return ResponseEntity.ok(userBenefits);

    }


}
