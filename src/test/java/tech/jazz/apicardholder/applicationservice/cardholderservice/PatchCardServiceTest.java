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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import tech.jazz.apicardholder.presentation.dto.UpdateLimitRequest;
import tech.jazz.apicardholder.presentation.dto.UpdateLimitResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.CardNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.InsufficientLimitException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PatchCardServiceTest {
    @Mock
    CardRepository cardRepository;
    @Mock
    CardHolderRepository cardHolderRepository;
    @Spy
    CardMapper cardMapper = new CardMapperImpl();
    @InjectMocks
    PatchCardService patchCardService;
    @Captor
    ArgumentCaptor<CardEntity> cardCaptor;

    @Test
    void should_update_limit() {
        UUID cardHolderId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        UpdateLimitRequest updateLimitRequest = new UpdateLimitRequest(BigDecimal.valueOf(300));

        Mockito.when(cardHolderRepository.findByCardHolderId(cardHolderId))
                .thenReturn(Optional.of(cardHolderEntityFactory(cardHolderId)));
        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(cardEntityFactory(cardId, cardHolderId)));
        Mockito.when(cardRepository.findByCardHolder_CardHolderIdAndCardIdNot(cardHolderId, cardId)).thenReturn(List.of());
        Mockito.when(cardRepository.save(cardCaptor.capture())).thenReturn(cardEntityFactory(cardId, cardHolderId));

        UpdateLimitResponse updateLimitResponse = patchCardService.updateLimit(cardHolderId, cardId, updateLimitRequest);

        assertNotNull(updateLimitResponse);
        assertNotNull(cardCaptor);
        assertEquals(cardCaptor.getValue().getLimit().setScale(2), BigDecimal.valueOf(300).setScale(2));
    }

    @Test
    void should_throw_CardHolderNotFoundException_when_id_dont_exists() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(CardHolderNotFoundException.class, () ->
                patchCardService.updateLimit(UUID.randomUUID(), UUID.randomUUID(),
                        new UpdateLimitRequest(BigDecimal.TEN)));
    }

    @Test
    void should_throw_CardNotFoundException_when_id_dont_exists() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(cardHolderEntityFactory(UUID.randomUUID())));
        Mockito.when(cardRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());


        assertThrows(CardNotFoundException.class, () ->
                patchCardService.updateLimit(UUID.randomUUID(), UUID.randomUUID(),
                        new UpdateLimitRequest(BigDecimal.TEN)));
    }

    @Test
    void should_throw_DivergentCardHolderException_when_cardHolderID_doesnt_match_with_request() {
        Mockito.when(cardHolderRepository.findByCardHolderId(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(cardHolderEntityFactory(UUID.randomUUID())));
        Mockito.when(cardRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(cardEntityFactory(UUID.randomUUID(), UUID.randomUUID())));

        assertThrows(DivergentCardHolderException.class, () ->
                patchCardService.updateLimit(UUID.randomUUID(), UUID.randomUUID(),
                        new UpdateLimitRequest(BigDecimal.TEN)));
    }

    @Test
    void should_throw_InsufficientLimitException_when_remaining_limit_is_insufficient_for_update() {
        UUID cardHolderId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        UpdateLimitRequest updateLimitRequest = new UpdateLimitRequest(BigDecimal.valueOf(300));

        Mockito.when(cardHolderRepository.findByCardHolderId(cardHolderId))
                .thenReturn(Optional.of(cardHolderEntityFactory(cardHolderId)));
        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(cardEntityFactory(cardId, cardHolderId)));
        Mockito.when(cardRepository.findByCardHolder_CardHolderIdAndCardIdNot(cardHolderId, cardId)).thenReturn(List.of(
                cardEntityFactory(UUID.randomUUID(), cardHolderId), cardEntityFactory(UUID.randomUUID(), cardHolderId)
                ));

        assertThrows(InsufficientLimitException.class, () ->
                patchCardService.updateLimit(cardHolderId, cardId, updateLimitRequest));
    }

    private CardHolderEntity cardHolderEntityFactory(UUID cardHolderId) {
        return CardHolderEntity.builder()
                .cardHolderId(cardHolderId)
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

    private CardEntity cardEntityFactory(UUID cardId, UUID cardHolderId){
        return CardEntity.builder()
                .cardId(cardId)
                .cardHolder(cardHolderEntityFactory(cardHolderId))
                .limit(BigDecimal.valueOf(200))
                .cardNumber("1234 1234 1234 1234")
                .cvv(123)
                .dueDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}