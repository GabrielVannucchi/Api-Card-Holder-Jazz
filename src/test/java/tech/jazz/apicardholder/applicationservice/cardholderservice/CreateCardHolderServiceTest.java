package tech.jazz.apicardholder.applicationservice.cardholderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import feign.RetryableException;
import java.math.BigDecimal;
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
import tech.jazz.apicardholder.applicationservice.creditapi.CreditAnalysisResponse;
import tech.jazz.apicardholder.applicationservice.creditapi.CreditApi;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapperImpl;
import tech.jazz.apicardholder.infrastructure.repository.BankAccountRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.BankAccountEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;
import tech.jazz.apicardholder.presentation.dto.CardHolderRequest;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCreditAnalysisAndClientException;
import tech.jazz.apicardholder.presentation.handler.exception.UnapprovedCreditAnalysisException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateCardHolderServiceTest {
    @Mock
    CreditApi creditApi;
    @Mock
    CardHolderRepository cardHolderRepository;
    @Mock
    BankAccountRepository bankAccountRepository;
    @Spy
    CardHolderMapper cardHolderMapper = new CardHolderMapperImpl();
    @InjectMocks
    CreateCardHolderService createCardHolderService;

    @Captor
    public ArgumentCaptor<UUID> uuidCaptor;

    @Test
    void should_create_card_holder_with_bank() {
        CardHolderRequest cardHolderRequest = cardHolderRequestFactoryWithBank();

        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(creditAnalysisResponseFactory(cardHolderRequest));
        Mockito.when(bankAccountRepository.save(Mockito.any(BankAccountEntity.class))).thenReturn(bankAccountEntityFactory());
        Mockito.when(cardHolderRepository.save(Mockito.any(CardHolderEntity.class))).thenReturn(cardHolderEntityFactory());

        CardHolderResponse cardHolderResponse = createCardHolderService.createCardHolder(cardHolderRequest);

        assertNotNull(cardHolderResponse);
        assertNotNull(cardHolderResponse.cardHolderId());
    }

    @Test
    void should_create_card_holder_without_bank(){
        CardHolderRequest cardHolderRequest = cardHolderRequestFactory();

        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(creditAnalysisResponseFactory(cardHolderRequest));
        Mockito.when(cardHolderRepository.save(Mockito.any(CardHolderEntity.class))).thenReturn(cardHolderEntityFactory());

        CardHolderResponse cardHolderResponse = createCardHolderService.createCardHolder(cardHolderRequest);

        assertNotNull(cardHolderResponse);
        assertNotNull(cardHolderResponse.cardHolderId());
    }
    @Test
    void should_throw_DivergentCreditAnalysisAndClientException_when_clientId_not_correspond_with_clientId_in_creditAnalysis(){
        CardHolderRequest cardHolderRequest = cardHolderRequestFactory();

        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(creditAnalysisResponseFactory(
                new CardHolderRequest(
                        UUID.randomUUID(),
                        cardHolderRequest.creditAnalysisId(),
                        null
                )
        ));

        assertThrows(DivergentCreditAnalysisAndClientException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest));
    }

    @Test
    void should_throw_UnapprovedCreditAnalysisException_when_creditAnalysis_is_unapproved(){
        CardHolderRequest cardHolderRequest = cardHolderRequestFactory();

        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(new CreditAnalysisResponse(
                cardHolderRequest.creditAnalysisId(),
                cardHolderRequest.clientId(),
                false,
                BigDecimal.valueOf(0)
        ));

        assertThrows(UnapprovedCreditAnalysisException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest));
    }

    public CardHolderRequest cardHolderRequestFactoryWithBank(){
        return new CardHolderRequest(UUID.randomUUID(), UUID.randomUUID(),
                new CardHolderRequest.BankAccount("12345678-9", "1234", "123"));
    }

    public CardHolderRequest cardHolderRequestFactory(){
        return new CardHolderRequest(UUID.randomUUID(), UUID.randomUUID(),null);
    }

    public CreditAnalysisResponse creditAnalysisResponseFactory(CardHolderRequest cardHolderRequest) {
        return new CreditAnalysisResponse(cardHolderRequest.creditAnalysisId(), cardHolderRequest.clientId(),
                true, BigDecimal.valueOf(6000.0));
    }

    public BankAccountEntity bankAccountEntityFactory() {
        return BankAccountEntity.builder()
                .bankAccountId(UUID.randomUUID())
                .account("12345678-9")
                .agency("1234")
                .bankCode("123")
                .build();
    }

    public CardHolderEntity cardHolderEntityFactory() {
        return CardHolderEntity.builder()
                .cardHolderId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .statusEnum(StatusEnum.ACTIVE)
                .limit(BigDecimal.valueOf(6000))
                .bankAccount(bankAccountEntityFactory())
                .build();
    }


}
