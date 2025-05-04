package com.example.demo.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Message {

    @NotBlank(message = "Sender is required")
    @Size(max = 50)
    private String sender;

    @NotBlank(message = "Recipient is required")
    @Size(max = 50)
    private String recipient;

    @NotBlank(message = "Message content is required")
    @Size(max = 500)
    private String content;

    public Message() {}

    public Message(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
