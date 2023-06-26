package tech.jazz.apicardholder.presentation.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateLimitResponse(
        UUID cardId,
        BigDecimal limit
) {
}
