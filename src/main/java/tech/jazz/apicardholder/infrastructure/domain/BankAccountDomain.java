package tech.jazz.apicardholder.infrastructure.domain;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import tech.jazz.apicardholder.infrastructure.repository.util.ValidationCustom;

public record BankAccountDomain(
        @Pattern(regexp = "^\\d{8}-\\d$|^\\d{9}$|^\\d{8}-[Xx]$", message = "Insert a valid Account Number")
        String account,
        @Pattern(regexp = "\\d{4}", message = "Insert a valid Agency")
        String agency,
        @Pattern(regexp = "\\d{3}", message = "Insert a valid bank code")
        String bankCode
) {
    @Builder
    public BankAccountDomain(String account, String agency, String bankCode) {
        this.account = account;
        this.agency = agency;
        this.bankCode = bankCode;
        ValidationCustom.validator(this);
    }
}
