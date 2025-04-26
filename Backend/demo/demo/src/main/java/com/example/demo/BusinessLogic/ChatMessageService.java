package com.example.demo.BusinessLogic;

import com.example.demo.Model.ChatMessage;
import com.example.demo.Repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesForRecipient(String recipient) {
        return chatMessageRepository.findByRecipient(recipient);
    }

    // ✅ NEW: Get full chat history
    public List<ChatMessage> getChatHistoryBetweenUsers(String sender, String recipient) {
        return chatMessageRepository.findChatHistory(sender, recipient);
    }
}
