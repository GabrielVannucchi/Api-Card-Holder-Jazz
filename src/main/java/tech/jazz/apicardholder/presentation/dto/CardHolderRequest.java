package tech.jazz.apicardholder.presentation.dto;

import java.util.UUID;

public record CardHolderRequest(
        UUID clientId,
        UUID creditAnalysisId,
        BankAccount bankAccount
) {
    public record BankAccount(
            String account,
            String agency,
            String bankCode
    ){
    }

}
