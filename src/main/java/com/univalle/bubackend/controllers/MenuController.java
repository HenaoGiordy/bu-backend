package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.auth.CreateMenuRequest;
import com.univalle.bubackend.services.menu.IMenuService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MenuController {
    private IMenuService menuService;

    @PostMapping
    public ResponseEntity<CreateMenuRequest> createMenu(@RequestBody CreateMenuRequest createMenuRequest) {
        return new ResponseEntity<>(menuService.createMenu(createMenuRequest), HttpStatus.CREATED);

    }

}
