package com.example.demo.Controllers;

import com.example.demo.Model.ChatMessage;
import com.example.demo.BusinessLogic.ChatMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ChatMessage saveMessage(@RequestBody ChatMessage message) {
        return chatMessageService.saveMessage(message);
    }

    @GetMapping("/{recipient}")
    public List<ChatMessage> getMessagesForRecipient(@PathVariable String recipient) {
        return chatMessageService.getMessagesForRecipient(recipient);
    }
}
