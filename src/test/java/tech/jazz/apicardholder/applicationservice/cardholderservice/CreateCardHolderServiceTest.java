package tech.jazz.apicardholder.applicationservice.cardholderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import feign.FeignException;
import feign.RetryableException;
import jakarta.validation.Valid;
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
import tech.jazz.apicardholder.presentation.handler.exception.CreditAnalysisApiUnavailableException;
import tech.jazz.apicardholder.presentation.handler.exception.CreditAnalysisNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCreditAnalysisAndClientException;
import tech.jazz.apicardholder.presentation.handler.exception.DuplicatedCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.IncompleteBanckAccountException;
import tech.jazz.apicardholder.presentation.handler.exception.InvalidCardHolderRequestException;
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

        Mockito.when(cardHolderRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(null);
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

        Mockito.when(cardHolderRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(null);
        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(creditAnalysisResponseFactory(cardHolderRequest));
        Mockito.when(cardHolderRepository.save(Mockito.any(CardHolderEntity.class))).thenReturn(cardHolderEntityFactory());

        CardHolderResponse cardHolderResponse = createCardHolderService.createCardHolder(cardHolderRequest);

        assertNotNull(cardHolderResponse);
        assertNotNull(cardHolderResponse.cardHolderId());
    }

    @Test
    void should_throw_DuplicatedCardHolderException_when_cardholder_already_is_registred(){
        CardHolderRequest cardHolderRequest = cardHolderRequestFactory();

        Mockito.when(cardHolderRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(cardHolderEntityFactory());

        assertThrows(DuplicatedCardHolderException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest));
    }
    @Test
    void should_throw_DivergentCreditAnalysisAndClientException_when_clientId_not_correspond_with_clientId_in_creditAnalysis(){
        CardHolderRequest cardHolderRequest = cardHolderRequestFactory();

        Mockito.when(cardHolderRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(null);
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

        Mockito.when(cardHolderRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(null);
        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(new CreditAnalysisResponse(
                cardHolderRequest.creditAnalysisId(),
                cardHolderRequest.clientId(),
                false,
                BigDecimal.valueOf(0)
        ));

        assertThrows(UnapprovedCreditAnalysisException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest));
    }

    @Test
    void should_throw_CreditAnalysisApiUnavailableException_when_CreditApi_is_out(){
        CardHolderRequest cardHolderRequest = cardHolderRequestFactoryWithBank();

        Mockito.when(cardHolderRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(null);
        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenThrow(RetryableException.class);

        assertThrows(CreditAnalysisApiUnavailableException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest));
    }
    @Test
    void should_throw_CreditAnalysisNotFoundException_when_creditAnalysis_not_found(){
        CardHolderRequest cardHolderRequest = cardHolderRequestFactoryWithBank();

        Mockito.when(cardHolderRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(null);
        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenThrow(FeignException.class);

        assertThrows(CreditAnalysisNotFoundException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest));
    }

    @Test
    void should_throw_IncompleteBanckAccountException_when_accountBank_is_imcomplete(){
        final CardHolderRequest cardHolderRequest = new CardHolderRequest(
                UUID.randomUUID(), UUID.randomUUID(), new CardHolderRequest.BankAccount(null, "1234", "123")
        );

        Mockito.when(cardHolderRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(null);
        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(creditAnalysisResponseFactory(cardHolderRequest));

        assertThrows(IncompleteBanckAccountException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest));

        final CardHolderRequest cardHolderRequest2 = new CardHolderRequest(
                UUID.randomUUID(), UUID.randomUUID(), new CardHolderRequest.BankAccount("12345678-9", null, "123")
        );
        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(creditAnalysisResponseFactory(cardHolderRequest2));

        assertThrows(IncompleteBanckAccountException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest2));

        final CardHolderRequest cardHolderRequest3 = new CardHolderRequest(
                UUID.randomUUID(), UUID.randomUUID(), new CardHolderRequest.BankAccount("12345678-9", "1234", null)
        );
        Mockito.when(creditApi.getAnalysis(Mockito.any(UUID.class))).thenReturn(creditAnalysisResponseFactory(cardHolderRequest3));

        assertThrows(IncompleteBanckAccountException.class, () -> createCardHolderService.createCardHolder(cardHolderRequest3));

    }

    private CardHolderRequest cardHolderRequestFactoryWithBank(){
        return new CardHolderRequest(UUID.randomUUID(), UUID.randomUUID(),
                new CardHolderRequest.BankAccount("12345678-9", "1234", "123"));
    }

    private CardHolderRequest cardHolderRequestFactory(){
        return new CardHolderRequest(UUID.randomUUID(), UUID.randomUUID(),null);
    }

    private CreditAnalysisResponse creditAnalysisResponseFactory(CardHolderRequest cardHolderRequest) {
        return new CreditAnalysisResponse(cardHolderRequest.creditAnalysisId(), cardHolderRequest.clientId(),
                true, BigDecimal.valueOf(6000.0));
    }

    private BankAccountEntity bankAccountEntityFactory() {
        return BankAccountEntity.builder()
                .bankAccountId(UUID.randomUUID())
                .account("12345678-9")
                .agency("1234")
                .bankCode("123")
                .build();
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


}
