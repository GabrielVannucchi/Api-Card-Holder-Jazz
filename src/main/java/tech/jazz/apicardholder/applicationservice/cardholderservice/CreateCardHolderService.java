package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apicardholder.applicationservice.creditapi.CreditAnalysisResponse;
import tech.jazz.apicardholder.applicationservice.creditapi.CreditApi;
import tech.jazz.apicardholder.infrastructure.domain.BankAccountDomain;
import tech.jazz.apicardholder.infrastructure.domain.CardHolderDomain;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.repository.BankAccountRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;
import tech.jazz.apicardholder.presentation.dto.CardHolderRequest;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCreditAnalysisAndClientException;
import tech.jazz.apicardholder.presentation.handler.exception.UnapprovedCreditAnalysisException;

@Service
@RequiredArgsConstructor
public class CreateCardHolderService {
    private final CardHolderRepository cardHolderRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CardHolderMapper cardHolderMapper;
    private final CreditApi creditApi;

    public CardHolderResponse createCardHolder(CardHolderRequest cardHolderRequest) {
        final BigDecimal clientLimit = getCreditAnalysisApi(cardHolderRequest).approvedLimit();

        final CardHolderDomain cardHolderDomain = CardHolderDomain.builder()
                .clientId(cardHolderRequest.clientId())
                .creditAnalysisId(cardHolderRequest.creditAnalysisId())
                .statusEnum(StatusEnum.ACTIVE)
                .limit(clientLimit)
                .build();

        CardHolderEntity cardHolderEntity = cardHolderMapper.from(cardHolderDomain);
        if (cardHolderRequest.bankAccount() != null) {

            final CardHolderDomain cardHolderDomainWithBank = cardHolderDomain.toBuilder()
                    .bankAccount(BankAccountDomain.builder()
                            .bankCode(cardHolderRequest.bankAccount().bankCode())
                            .agency(cardHolderRequest.bankAccount().agency())
                            .account(cardHolderRequest.bankAccount().account())
                            .build())
                    .build();
            cardHolderEntity = cardHolderMapper.from(cardHolderDomainWithBank);
            bankAccountRepository.save(cardHolderEntity.getBankAccount());
        }
        cardHolderEntity = cardHolderRepository.save(cardHolderEntity);
        return cardHolderMapper.from(cardHolderEntity);
    }

    private CreditAnalysisResponse getCreditAnalysisApi(CardHolderRequest cardHolderRequest) {
        final CreditAnalysisResponse creditAnalysis = creditApi.getAnalysis(cardHolderRequest.creditAnalysisId());
        if (!creditAnalysis.clientId().equals(cardHolderRequest.clientId())) {
            throw new DivergentCreditAnalysisAndClientException("Client id does not correspond with this Credit Analysis");
        }
        if (!creditAnalysis.approved()) {
            throw new UnapprovedCreditAnalysisException("Card Holder negated due to unapproved Credit Analysis");
        }
        return creditAnalysis;
    }

}
