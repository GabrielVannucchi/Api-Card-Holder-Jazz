package tech.jazz.apicardholder.infrastructure.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

public record CardDomain(
        CardHolderDomain cardHolder,
        BigDecimal limit,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate
) {
    @Builder(toBuilder = true)
    public CardDomain(CardHolderDomain cardHolder, BigDecimal limit, String cardNumber, Integer cvv, LocalDate dueDate) {
        this.cardHolder = cardHolder;
        this.limit = limit;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
    }
}
