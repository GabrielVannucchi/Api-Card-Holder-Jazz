package tech.jazz.apicardholder.applicationservice.cardholderservice;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
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

        List<CardHolderResponse> cardHolderResponses = searchCardHolderService.listAll();

        assertNotNull(cardHolderResponses);
        assertEquals(3, cardHolderResponses.size());
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