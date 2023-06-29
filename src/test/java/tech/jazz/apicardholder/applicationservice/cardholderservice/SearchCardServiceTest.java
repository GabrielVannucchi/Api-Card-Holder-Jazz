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
import tech.jazz.apicardholder.infrastructure.repository.entity.BankAccountEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;
import tech.jazz.apicardholder.presentation.dto.CardResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.CardNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCardHolderException;

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

    @Test
    void should_find_card_with_given_id() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(
                Optional.of(
                        CardHolderEntity.builder()
                                .cardHolderId(UUID.fromString("12341234-1234-1234-1234-123412341234"))
                                .build()
                ));
        Mockito.when(cardRepository.findById(Mockito.any(UUID.class))).thenReturn(
                Optional.of(
                        CardEntity.builder()
                                .cardId(UUID.randomUUID())
                                .cardHolder(CardHolderEntity.builder()
                                        .cardHolderId(UUID.fromString("12341234-1234-1234-1234-123412341234"))
                                        .build())
                                .limit(BigDecimal.valueOf(200))
                                .cardNumber("1234 1234 1234 1234")
                                .cvv(123)
                                .dueDate(LocalDate.now())
                                .build()
                ));

        UUID cardHolderId = UUID.fromString("12341234-1234-1234-1234-123412341234");

        CardResponse cardResponse = searchCardService.findById(cardHolderId, UUID.randomUUID());

        assertNotNull(cardResponse);
        assertNotNull(cardResponse.cardId());
        assertNotNull(cardResponse.cardNumber());
        assertNotNull(cardResponse.cvv());
        assertNotNull(cardResponse.dueDate());


    }

    @Test
    void should_throw_CardHolderNotFoundException_when_cardHolderId_dont_exist() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(CardHolderNotFoundException.class, () -> searchCardService.findById(UUID.randomUUID(), UUID.randomUUID()));

    }

    @Test
    void should_throw_CardNotFoundException_when_cardId_dont_exist() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(
                Optional.of(
                        CardHolderEntity.builder()
                                .cardHolderId(UUID.fromString("12341234-1234-1234-1234-123412341234"))
                                .build()
                ));
        Mockito.when(cardRepository.findById(Mockito.any(UUID.class))).thenReturn(
                Optional.empty());

        assertThrows(CardNotFoundException.class, () -> searchCardService.findById(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void should_throw_DivergentCardHolderException_when_cardHolderId_dont_correspond_with_cardHolderId_in_cardEntity() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(
                Optional.of(
                        CardHolderEntity.builder()
                                .cardHolderId(UUID.fromString("12341234-1234-1234-1234-123412341234"))
                                .build()
                ));
        Mockito.when(cardRepository.findById(Mockito.any(UUID.class))).thenReturn(
                Optional.of(
                        cardEntityFactory()
                ));


        assertThrows(DivergentCardHolderException.class, () -> searchCardService.findById(UUID.randomUUID(), UUID.randomUUID()));

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