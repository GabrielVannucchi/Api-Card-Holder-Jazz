package tech.jazz.apicardholder.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;

public record CardHolderResponse(
        UUID cardHolderId,
        UUID creditAnalysisId,
        StatusEnum statusEnum,
        BigDecimal limit,
        LocalDateTime createdAt
) {

    @Builder
    public CardHolderResponse(UUID cardHolderId, UUID creditAnalysisId, StatusEnum statusEnum, BigDecimal limit, LocalDateTime createdAt) {
        this.cardHolderId = cardHolderId;
        this.creditAnalysisId = creditAnalysisId;
        this.statusEnum = statusEnum;
        this.limit = limit;
        this.createdAt = createdAt;
    }

}
