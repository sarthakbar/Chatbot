package com.cb.controller;

import com.cb.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        String reply = chatService.getBotResponse(userMessage);
        return ResponseEntity.ok(reply);
    }
}
