package com.univalle.bubackend.controllers.odontology;


import com.univalle.bubackend.DTOs.odontology.UserResponse;
import com.univalle.bubackend.DTOs.odontology.VisitLogRequest;
import com.univalle.bubackend.DTOs.odontology.VisitLogResponse;
import com.univalle.bubackend.services.odontology.OdontologyVisitLogImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/odontology-visits")
@AllArgsConstructor
public class OdontologyVisitLogController {

    private OdontologyVisitLogImpl odontologyVisitLog;

    @GetMapping("/search/{username}")
    public ResponseEntity<UserResponse> searchStudentsByCode(@PathVariable String username) {
        UserResponse response = odontologyVisitLog.findStudentsByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
        
    @PostMapping("/register")
    public ResponseEntity<VisitLogResponse> registerActivity(@RequestBody VisitLogRequest request) {
        VisitLogResponse response = odontologyVisitLog.registerVisit(request);
        return ResponseEntity.ok(response);
    }
}
