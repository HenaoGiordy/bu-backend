package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.CreateMenuRequest;
import com.univalle.bubackend.services.menu.IMenuService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "Security Token")
public class MenuController {
    private IMenuService menuService;

    @PostMapping
    public ResponseEntity<CreateMenuRequest> createMenu(@RequestBody CreateMenuRequest createMenuRequest) {
        return new ResponseEntity<>(menuService.createMenu(createMenuRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CreateMenuRequest>> getMenu() {
        return new ResponseEntity<>(menuService.getMenu(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CreateMenuRequest> editMenu(@Valid @RequestBody CreateMenuRequest createMenuRequest) {
        return new ResponseEntity<>(menuService.editMenu(createMenuRequest), HttpStatus.OK);
    }

}
