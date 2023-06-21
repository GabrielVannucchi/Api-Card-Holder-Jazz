package tech.jazz.apicardholder.infrastructure.domain;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;

public record CardHolderDomain(
        UUID clientId,
        StatusEnum statusEnum,
        BigDecimal limit,
        BankAccountDomain bankAccount
) {
    @Builder(toBuilder = true)
    public CardHolderDomain(UUID clientId, StatusEnum statusEnum, BigDecimal limit, BankAccountDomain bankAccount) {
        this.clientId = clientId;
        this.statusEnum = statusEnum;
        this.limit = limit;
        this.bankAccount = bankAccount;
    }
}
