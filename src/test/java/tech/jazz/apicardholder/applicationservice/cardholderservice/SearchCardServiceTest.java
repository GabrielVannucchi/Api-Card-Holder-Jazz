package tech.jazz.apicardholder.applicationservice.cardholderservice;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapper;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapperImpl;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;
import tech.jazz.apicardholder.presentation.dto.CardResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SearchCardServiceTest {
    @Mock
    CardRepository cardRepository;
    @Mock
    CardHolderRepository cardHolderRepository;
    @Spy
    CardMapper cardMapper = new CardMapperImpl();
    @InjectMocks
    SearchCardService searchCardService;

    @Test
    void should_list_all() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(Optional.of(cardHolderEntityFactory()));
        Mockito.when(cardRepository.findByCardHolder_CardHolderId(Mockito.any(UUID.class))).thenReturn(List.of(cardEntityFactory(), cardEntityFactory(), cardEntityFactory()));

        List<CardResponse> cardResponses = searchCardService.listAllByCardHolder(UUID.randomUUID());

        assertNotNull(cardResponses);
        assertNotNull(cardResponses.get(0));
        assertEquals(cardResponses.size(), 3);
    }

    @Test
    void should_throw_CardHolderNotFoundException_when_cardHolderId_dont_exists() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(CardHolderNotFoundException.class, () -> searchCardService.listAllByCardHolder(UUID.randomUUID()));
    }

    private CardHolderEntity cardHolderEntityFactory() {
        return CardHolderEntity.builder()
                .cardHolderId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .statusEnum(StatusEnum.ACTIVE)
                .limit(BigDecimal.valueOf(600))
                .bankAccount(null)
                .build();
    }

    private CardEntity cardEntityFactory(){
        return CardEntity.builder()
                .cardId(UUID.randomUUID())
                .cardHolder(cardHolderEntityFactory())
                .limit(BigDecimal.valueOf(200))
                .cardNumber("1234 1234 1234 1234")
                .cvv(123)
                .dueDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}