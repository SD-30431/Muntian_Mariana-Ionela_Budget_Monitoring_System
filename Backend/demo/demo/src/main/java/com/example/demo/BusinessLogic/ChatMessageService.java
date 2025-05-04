package com.example.demo.BusinessLogic;

import com.example.demo.Model.ChatMessage;
import com.example.demo.Repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(ChatMessage message) {
        if (!StringUtils.hasText(message.getSender())) {
            throw new IllegalArgumentException("Sender must not be blank");
        }
        if (!StringUtils.hasText(message.getRecipient())) {
            throw new IllegalArgumentException("Recipient must not be blank");
        }
        if (!StringUtils.hasText(message.getContent())) {
            throw new IllegalArgumentException("Message content must not be blank");
        }

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesForRecipient(String recipient) {
        return chatMessageRepository.findByRecipient(recipient);
    }

    public List<ChatMessage> getChatHistoryBetweenUsers(String sender, String recipient) {
        return chatMessageRepository.findChatHistory(sender, recipient);
    }
}
