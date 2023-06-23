package tech.jazz.apicardholder.applicationservice.cardholderservice;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
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
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.BankAccountEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.StatusOutOfFormatException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SearchCardHolderServiceTest {

    @Mock
    CardHolderRepository cardHolderRepository;
    @Spy
    CardHolderMapper cardHolderMapper = new CardHolderMapperImpl();
    @InjectMocks
    SearchCardHolderService searchCardHolderService;

    @Test
    void should_return_all_cardHolders(){
        Mockito.when(cardHolderRepository.findAll()).thenReturn(
                List.of(cardHolderEntityFactory(), cardHolderEntityFactory(), cardHolderEntityFactory()));

        List<CardHolderResponse> cardHolderResponses = searchCardHolderService.listAll(null);

        assertNotNull(cardHolderResponses);
        assertEquals(3, cardHolderResponses.size());
    }

    @Test
    void should_return_all_active_cardHolders(){
        Mockito.when(cardHolderRepository.findByStatusEnum(Mockito.any(StatusEnum.class))).thenReturn(
                List.of(cardHolderEntityFactory(), cardHolderEntityFactory(), cardHolderEntityFactory()));

        List<CardHolderResponse> cardHolderResponses = searchCardHolderService.listAll("active");

        assertNotNull(cardHolderResponses);
        assertEquals(3, cardHolderResponses.size());
    }

    @Test
    void should_return_all_inactive_cardHolders(){
        Mockito.when(cardHolderRepository.findByStatusEnum(Mockito.any(StatusEnum.class))).thenReturn(
                List.of(cardHolderEntityFactory(), cardHolderEntityFactory(), cardHolderEntityFactory()));

        List<CardHolderResponse> cardHolderResponses = searchCardHolderService.listAll("inactive");

        assertNotNull(cardHolderResponses);
        assertEquals(3, cardHolderResponses.size());
    }
    @Test
    void should_throw_StatusOutOfFormatException_when_status_is_out_of_enum(){
        assertThrows(StatusOutOfFormatException.class, () -> searchCardHolderService.listAll("aaaaaaa"));
    }
    @Test
    void should_return_given_cardHolder(){
        Mockito.when(cardHolderRepository.findByCardHolderId(UUID.fromString("12341234-1234-1234-1234-123412341234"))).thenReturn(Optional.of(cardHolderEntityFactory()));
        CardHolderResponse cardHolderResponse = searchCardHolderService.findCardHolder(UUID.fromString("12341234-1234-1234-1234-123412341234"));

        assertNotNull(cardHolderResponse);
        assertNotNull(cardHolderResponse.cardHolderId());
    }

    @Test
    void should_throw_CardHolderNotFoundException_when_id_dont_exist(){
        Mockito.when(cardHolderRepository.findByCardHolderId(UUID.fromString("12341234-1234-1234-1234-123412341234"))).thenReturn(Optional.empty());
        assertThrows(CardHolderNotFoundException.class,
                () -> searchCardHolderService.findCardHolder(UUID.fromString("12341234-1234-1234-1234-123412341234")));
    }

    private CardHolderEntity cardHolderEntityFactory() {
        return CardHolderEntity.builder()
                .cardHolderId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .statusEnum(StatusEnum.ACTIVE)
                .limit(BigDecimal.valueOf(6000))
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

}