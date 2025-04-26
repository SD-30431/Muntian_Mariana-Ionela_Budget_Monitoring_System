package com.example.demo.Repository;

import com.example.demo.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRecipient(String recipient);

    // ✅ NEW: Fetch history between two users
    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender = :sender AND m.recipient = :recipient) " +
            "OR (m.sender = :recipient AND m.recipient = :sender) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(@Param("sender") String sender, @Param("recipient") String recipient);
}
