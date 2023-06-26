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
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapperImpl;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapper;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapperImpl;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.BankAccountEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;
import tech.jazz.apicardholder.presentation.dto.CardRequest;
import tech.jazz.apicardholder.presentation.dto.CardResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.InsufficientLimitException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateCardServiceTest {
    @Mock
    CardRepository cardRepository;
    @Mock
    CardHolderRepository cardHolderRepository;
    @Spy
    CardMapper cardMapper = new CardMapperImpl();
    @Spy
    CardHolderMapper cardHolderMapper = new CardHolderMapperImpl();
    @InjectMocks
    CreateCardService createCardService;

    @Test
    void should_create_card() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(Optional.of(cardHolderEntityFactory()));
        Mockito.when(cardRepository.findByCardHolder_CardHolderId(Mockito.any(UUID.class))).thenReturn(List.of(cardEntityFactory(), cardEntityFactory()));
        Mockito.when(cardRepository.save(Mockito.any(CardEntity.class))).thenReturn(cardEntityFactory());

        UUID cardHolderId = UUID.fromString("12341234-1234-1234-1234-123412341234");
        CardRequest cardRequest = new CardRequest(cardHolderId, BigDecimal.valueOf(200));

        CardResponse cardResponse = createCardService.createCard(cardHolderId, cardRequest);

        assertNotNull(cardResponse);
        assertNotNull(cardResponse.cardId());
        assertNotNull(cardResponse.cardNumber());
        assertNotNull(cardResponse.cvv());
        assertNotNull(cardResponse.dueDate());
    }

    @Test
    void should_throw_DivergentCardHolderException_when_cardHolderId_is_different_from_request() {
        UUID cardHolderId = UUID.randomUUID();
        CardRequest cardRequest = new CardRequest(UUID.randomUUID(), BigDecimal.valueOf(200));

        assertThrows(DivergentCardHolderException.class, () -> createCardService.createCard(cardHolderId, cardRequest));
    }

    @Test
    void should_throw_CardHolderNotFoundException_when_no_cardHolder_found_with_given_id() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        UUID cardHolderId = UUID.fromString("12341234-1234-1234-1234-123412341234");
        CardRequest cardRequest = new CardRequest(cardHolderId, BigDecimal.valueOf(200));

        assertThrows(CardHolderNotFoundException.class, () -> createCardService.createCard(cardHolderId, cardRequest));
    }

    @Test
    void should_throw_InsufficientLimitException_when_cardHolder_dont_have_more_limit_than_request() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(Optional.of(cardHolderEntityFactory()));
        Mockito.when(cardRepository.findByCardHolder_CardHolderId(Mockito.any(UUID.class))).thenReturn(List.of(cardEntityFactory(), cardEntityFactory(), cardEntityFactory()));

        UUID cardHolderId = UUID.fromString("12341234-1234-1234-1234-123412341234");
        CardRequest cardRequest = new CardRequest(cardHolderId, BigDecimal.valueOf(200));

        assertThrows(InsufficientLimitException.class, () -> createCardService.createCard(cardHolderId, cardRequest));

    }

    private CardHolderEntity cardHolderEntityFactory() {
        return CardHolderEntity.builder()
                .cardHolderId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .statusEnum(StatusEnum.ACTIVE)
                .limit(BigDecimal.valueOf(600))
                .bankAccount(bankAccountEntityFactory())
                .build();
    }

    private BankAccountEntity bankAccountEntityFactory() {
        return BankAccountEntity.builder()
                .bankAccountId(UUID.randomUUID())
                .account("12345678-9")
                .agency("1234")
                .bankCode("123")
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