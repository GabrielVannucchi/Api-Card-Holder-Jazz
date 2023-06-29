package tech.jazz.apicardholder.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import tech.jazz.apicardholder.infrastructure.repository.util.ValidationCustom;

public record CardHolderRequest(
        @NotNull
        UUID clientId,
        @NotNull
        UUID creditAnalysisId,
        BankAccount bankAccount
) {
    public record BankAccount(
            String account,
            String agency,
            String bankCode
    ) {
        public BankAccount(String account, String agency, String bankCode) {
            this.account = account;
            this.agency = agency;
            this.bankCode = bankCode;
            ValidationCustom.validator(this);
        }
    }

}
