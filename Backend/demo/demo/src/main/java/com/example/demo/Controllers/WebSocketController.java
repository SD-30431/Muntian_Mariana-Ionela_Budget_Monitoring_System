package com.example.demo.Controllers;

import com.example.demo.DTO.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message broadcastMessage(Message message) {
        return message;
    }
}
