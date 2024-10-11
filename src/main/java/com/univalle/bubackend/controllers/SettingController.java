package com.univalle.bubackend.controllers;

import com.univalle.bubackend.DTOs.setting.SettingRequest;
import com.univalle.bubackend.DTOs.setting.SettingResponse;
import com.univalle.bubackend.services.setting.ISettingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setting")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "Security Token")
public class SettingController {

    private ISettingService settingService;

    @PostMapping
    public ResponseEntity<SettingResponse> createSetting(@RequestBody SettingRequest settingRequest) {
        return new ResponseEntity<>(settingService.createSetting(settingRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SettingResponse>> getSetting() {
        return new ResponseEntity<>(settingService.getSetting(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SettingResponse> editSetting(@Valid @RequestBody SettingRequest settingRequest) {
        return new ResponseEntity<>(settingService.editSetting(settingRequest), HttpStatus.OK);
    }


}
