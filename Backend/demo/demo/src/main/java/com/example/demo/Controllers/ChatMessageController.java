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
        // Log the incoming message
        System.out.println("💬 Received Message:");
        System.out.println("Sender: " + message.getSender());
        System.out.println("Recipient: " + message.getRecipient());
        System.out.println("Content: " + message.getContent());

        // Basic validation
        if (message.getSender() == null || message.getSender().isBlank()
                || message.getRecipient() == null || message.getRecipient().isBlank()
                || message.getContent() == null || message.getContent().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Sender, recipient, and content must not be empty.");
        }

        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/{recipient}")
    public List<ChatMessage> getMessagesForRecipient(@PathVariable String recipient) {
        return chatMessageService.getMessagesForRecipient(recipient);
    }

    @GetMapping("/chat/history")
    public List<ChatMessage> getChatHistory(
            @RequestParam String sender,
            @RequestParam String recipient
    ) {
        return chatMessageService.getChatHistoryBetweenUsers(sender, recipient);
    }
}
