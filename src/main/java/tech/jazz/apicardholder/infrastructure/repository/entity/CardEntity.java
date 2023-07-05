package tech.jazz.apicardholder.infrastructure.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "card")
@Getter
@NoArgsConstructor
@Immutable
public class CardEntity {
    @Id
    @Column(name = "card_id")
    UUID cardId;

    @ManyToOne
    @JoinColumn(name = "card_holder_fk")
    CardHolderEntity cardHolder;

    @Column(name = "limit_value")
    BigDecimal limit;

    @Column(name = "card_number")
    String cardNumber;

    Integer cvv;

    @Column(name = "due_date")
    LocalDate dueDate;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Builder(toBuilder = true)
    public CardEntity(UUID cardId, CardHolderEntity cardHolder, BigDecimal limit, String cardNumber, Integer cvv, LocalDate dueDate,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (cardId == null) {
            this.cardId = UUID.randomUUID();
        } else {
            this.cardId = cardId;
        }
        this.cardHolder = cardHolder;
        this.limit = limit;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}
