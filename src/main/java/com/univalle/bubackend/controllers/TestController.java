package com.univalle.bubackend.controllers;

import com.univalle.bubackend.websocket.WebSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final WebSocketHandler webSocketHandler;

    public TestController(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping("/broadcast")
    public ResponseEntity<String> testBroadcast() {
        String message = "{\"remainingSlotsLunch\": 10, \"remainingSlotsSnack\": 5}";
        webSocketHandler.broadcast(message);
        return ResponseEntity.ok("Mensaje enviado: " + message);
    }
}
