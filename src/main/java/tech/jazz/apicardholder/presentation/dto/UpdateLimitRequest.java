package tech.jazz.apicardholder.presentation.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import tech.jazz.apicardholder.infrastructure.repository.util.ValidationCustom;

public record UpdateLimitRequest(
        @Positive
        BigDecimal limit
) {
    public UpdateLimitRequest(BigDecimal limit) {
        this.limit = limit;
        ValidationCustom.validator(this);
    }
}
