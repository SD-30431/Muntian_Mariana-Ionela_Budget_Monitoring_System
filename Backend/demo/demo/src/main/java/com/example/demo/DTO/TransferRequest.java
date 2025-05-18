package com.example.demo.DTO;

import javax.validation.constraints.*;

public class TransferRequest {
    @NotBlank(message = "Sender card number is required")
    @Size(min = 1, max = 20, message = "Sender card number must be between 1 and 20 characters")
    private String fromCard;

    @NotBlank(message = "Recipient card number is required")
    @Size(min = 1, max = 20, message = "Recipient card number must be between 1 and 20 characters")
    private String toCard;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private Double amount;

    // Getters and setters
    public String getFromCard() {
        return fromCard;
    }

    public void setFromCard(String fromCard) {
        this.fromCard = fromCard;
    }

    public String getToCard() {
        return toCard;
    }

    public void setToCard(String toCard) {
        this.toCard = toCard;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
