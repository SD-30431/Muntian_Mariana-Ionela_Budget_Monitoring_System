package com.example.demo.Controllers;

import com.example.demo.Model.ChatMessage;
import com.example.demo.BusinessLogic.ChatMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<?> saveMessage(@RequestBody ChatMessage message) {
        try {
            ChatMessage savedMessage = chatMessageService.saveValidatedMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/{recipient}")
    public ResponseEntity<List<ChatMessage>> getMessagesForRecipient(@PathVariable String recipient) {
        return ResponseEntity.ok(chatMessageService.getMessagesForRecipient(recipient));
    }

    @GetMapping("/chat/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @RequestParam String sender,
            @RequestParam String recipient
    ) {
        return ResponseEntity.ok(chatMessageService.getChatHistoryBetweenUsers(sender, recipient));
    }
}
