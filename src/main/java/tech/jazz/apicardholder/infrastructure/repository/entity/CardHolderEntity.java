package tech.jazz.apicardholder.infrastructure.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;

@Entity
@Table(name = "CARD_HOLDER")
@NoArgsConstructor
@Getter
public class CardHolderEntity {

    @Id
    @Column(name = "card_holder_id")
    UUID cardHolderId;

    @Column(name = "client_id")
    UUID clientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    StatusEnum statusEnum;

    @Column(name = "limit_value")
    BigDecimal limit;

    @OneToOne
    @JoinColumn(name = "bank_account_fk")
    BankAccountEntity bankAccount;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Builder(toBuilder = true)
    public CardHolderEntity(UUID cardHolderId, UUID clientId, StatusEnum statusEnum, BigDecimal limit, BankAccountEntity bankAccount) {
        this.cardHolderId = UUID.randomUUID();
        this.clientId = clientId;
        this.statusEnum = statusEnum;
        this.limit = limit;
        this.bankAccount = bankAccount;
    }
}
