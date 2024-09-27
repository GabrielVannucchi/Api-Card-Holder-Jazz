package tech.jazz.apicardholder.infrastructure.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapper;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapperImpl;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;

class CardDomainTest {
    @Test
    void should_build_object_with_parameters() {
        LocalDate dueDate = LocalDate.now();
        String cardNumber = "1234 1234 1234 1234";
        int cvv = 123;
        CardDomain cardDomain = CardDomain.builder()
                .cardHolderId(UUID.randomUUID())
                .limit(BigDecimal.TEN)
                .cardNumber(cardNumber)
                .cvv(cvv)
                .dueDate(dueDate)
                .build();

        assertEquals(cvv, cardDomain.cvv());
        assertEquals(cardNumber, cardDomain.cardNumber());
        assertEquals(dueDate, cardDomain.dueDate());
    }

}