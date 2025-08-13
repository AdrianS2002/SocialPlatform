

package com.example.social_platform.controller;

import com.example.social_platform.config.SecurityUtils;
import com.example.social_platform.controller.model.ChatMessage;
import com.example.social_platform.persistance.repository.AuthPsqlRepository;

import com.example.social_platform.service.ports.incoming.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final AuthPsqlRepository authRepo;

    public ChatController(ChatService chatService,
                          AuthPsqlRepository authRepo) {
        this.chatService = chatService;
        this.authRepo    = authRepo;
    }

    /** 1) Trimite un mesaj */
    @PostMapping("/send")
    public ResponseEntity<ChatMessage> send(
            @RequestParam Long to,
            @RequestBody String content
    ) {
        Long me = getCurrentUserId();
        ChatMessage sent = chatService.sendMessage(me, to, content);
        return ResponseEntity.ok(sent);
    }


    @GetMapping("/receive")
    public ResponseEntity<List<ChatMessage>> receive(
            @RequestParam Long with
    ) {
        Long me = getCurrentUserId();
        List<ChatMessage> history = chatService.receiveMessages(me, with);
        return ResponseEntity.ok(history);
    }


    private Long getCurrentUserId() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
        return authRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}