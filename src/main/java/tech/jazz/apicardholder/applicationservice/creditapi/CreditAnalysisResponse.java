package tech.jazz.apicardholder.applicationservice.creditapi;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditAnalysisResponse(
        UUID id,
        UUID clientId,
        Boolean approved,
        BigDecimal approvedLimit
) {

}
