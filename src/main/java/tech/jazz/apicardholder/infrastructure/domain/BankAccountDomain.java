package tech.jazz.apicardholder.infrastructure.domain;

import lombok.Builder;
import tech.jazz.apicardholder.presentation.handler.exception.BankAccountInvalidDataException;

public record BankAccountDomain(
        String account,
        String agency,
        String bankCode
) {
    @Builder
    public BankAccountDomain(String account, String agency, String bankCode) {
        if (account != null) {
            if (account.length() > 10 || account.length() < 0) {
                throw new BankAccountInvalidDataException("Please enter a valid Account Number");
            }
        }
        if (agency != null) {
            if (agency.length() > 4 || agency.length() < 0) {
                throw new BankAccountInvalidDataException("Please enter a valid Agency");
            }
        }
        if (bankCode != null) {
            if (bankCode.length() > 3 || bankCode.length() < 0) {
                throw new BankAccountInvalidDataException("Please enter a valid Bank Code");
            }
        }
        this.account = account;
        this.agency = agency;
        this.bankCode = bankCode;
    }
}
