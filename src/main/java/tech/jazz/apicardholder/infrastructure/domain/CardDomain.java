package tech.jazz.apicardholder.infrastructure.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Builder;

public record CardDomain(
        UUID cardHolderId,
        BigDecimal limit,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate
) {
    @Builder(toBuilder = true)
    public CardDomain(UUID cardHolderId, BigDecimal limit, String cardNumber, Integer cvv, LocalDate dueDate) {
        this.cardHolderId = cardHolderId;
        this.limit = limit;
        this.cardNumber = generateCardNumber();
        this.cvv = generateCvv();
        this.dueDate = generateDueDate();
    }

    private LocalDate generateDueDate() {
        return LocalDate.now().plusYears(5);
    }

    private String generateCardNumber() {
        final List<Integer> cardNumber = new ArrayList<>();
        cardNumber.add(4);
        for (int i = 0; i < 14; i++) {
            cardNumber.add(ThreadLocalRandom.current().nextInt(10));
        }
        Integer sum = 0;
        Integer verifier = 0;
        for (int i = 0; i < cardNumber.size(); i++) {
            if (i % 2 == 0) {
                sum += cardNumber.get(i);
            } else {
                Integer aux = cardNumber.get(i) * 2;
                if (aux > 9) {
                    aux = cardNumber.get(i) - 9;
                }
                sum += aux;
            }

        }
        verifier = sum;
        while (verifier > 10) {
            verifier -= 10;
        }
        verifier = 10 - verifier;
        String cardNumberFinal = "";
        Integer cont = 0;
        for (Integer i:
                cardNumber) {
            cardNumberFinal += i;
            cont++;
            if (cont == 4) {
                cont = 0;
                cardNumberFinal += " ";
            }
        }
        cardNumberFinal += verifier;
        return cardNumberFinal;
    }

    private Integer generateCvv() {
        return ThreadLocalRandom.current().nextInt(1000);
    }
}
