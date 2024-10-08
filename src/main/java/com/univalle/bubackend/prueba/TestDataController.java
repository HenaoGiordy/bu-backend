package com.univalle.bubackend.prueba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestDataController {

    @Autowired
    private TestDataService testDataService;

    @PostMapping("/create")
    public ResponseEntity<String> createTestData() {
        testDataService.createTestData();
        return new ResponseEntity<>("Datos de prueba creados", HttpStatus.CREATED);
    }
}
