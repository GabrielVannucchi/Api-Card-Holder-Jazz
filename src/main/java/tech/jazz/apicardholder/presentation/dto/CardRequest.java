package tech.jazz.apicardholder.presentation.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;
import tech.jazz.apicardholder.infrastructure.repository.util.ValidationCustom;

public record CardRequest(
        UUID cardHolderId,
        @Positive
        BigDecimal limit
) {
    public CardRequest(UUID cardHolderId, BigDecimal limit) {
        this.cardHolderId = cardHolderId;
        this.limit = limit;
        ValidationCustom.validator(this);
    }
}
