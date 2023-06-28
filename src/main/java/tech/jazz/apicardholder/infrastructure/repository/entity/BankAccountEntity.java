package tech.jazz.apicardholder.infrastructure.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "BANK_ACCOUNT")
@NoArgsConstructor
@Getter
@Immutable
public class BankAccountEntity {
    @Id
    @Column(name = "bank_account_id")
    UUID bankAccountId;
    @Column(name = "account_number")
    String account;
    String agency;
    @Column(name = "bank_code")
    String bankCode;

    @Builder
    public BankAccountEntity(UUID bankAccountId, String account, String agency, String bankCode) {
        this.bankAccountId = UUID.randomUUID();
        this.account = account;
        this.agency = agency;
        this.bankCode = bankCode;
    }
}
