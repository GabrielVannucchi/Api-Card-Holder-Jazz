package tech.jazz.apicardholder.applicationservice.cardholderservice;

import feign.FeignException;
import feign.RetryableException;
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
import tech.jazz.apicardholder.presentation.handler.exception.CreditAnalysisApiUnavailableException;
import tech.jazz.apicardholder.presentation.handler.exception.CreditAnalysisNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCreditAnalysisAndClientException;
import tech.jazz.apicardholder.presentation.handler.exception.DuplicatedCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.IncompleteBanckAccountException;
import tech.jazz.apicardholder.presentation.handler.exception.UnapprovedCreditAnalysisException;

@Service
@RequiredArgsConstructor
public class CreateCardHolderService {
    private final CardHolderRepository cardHolderRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CardHolderMapper cardHolderMapper;
    private final CreditApi creditApi;

    public CardHolderResponse createCardHolder(CardHolderRequest cardHolderRequest) {
        if (cardHolderRepository.findFirstByClientId(cardHolderRequest.clientId()) != null) {
            throw new DuplicatedCardHolderException("There's already a Card Holder with this Client Id");
        }
        final BigDecimal clientLimit = getCreditAnalysisApi(cardHolderRequest).approvedLimit();

        final CardHolderDomain cardHolderDomain = CardHolderDomain.builder()
                .clientId(cardHolderRequest.clientId())
                .statusEnum(StatusEnum.ACTIVE)
                .limit(clientLimit)
                .build();

        if (cardHolderRequest.bankAccount() != null) {
            if (isBankAccountComplete(cardHolderRequest.bankAccount())) {
                final CardHolderDomain cardHolderDomainWithBank = cardHolderDomain.toBuilder()
                        .bankAccount(BankAccountDomain.builder()
                                .bankCode(cardHolderRequest.bankAccount().bankCode())
                                .agency(cardHolderRequest.bankAccount().agency())
                                .account(cardHolderRequest.bankAccount().account())
                                .build())
                        .build();
                final CardHolderEntity cardHolderEntity = cardHolderMapper.from(cardHolderDomainWithBank);

                bankAccountRepository.save(cardHolderEntity.getBankAccount());
                final CardHolderResponse cardHolderResponse = cardHolderMapper.from(cardHolderRepository.save(cardHolderEntity));
                return cardHolderResponse;
            } else {
                throw new IncompleteBanckAccountException("Insert all data for bankAccount");
            }
        }

        final CardHolderEntity cardHolderEntity = cardHolderMapper.from(cardHolderDomain).toBuilder()
                .bankAccount(null)
                .build();
        return cardHolderMapper.from(cardHolderRepository.save(cardHolderEntity));
    }

    private boolean isBankAccountComplete(CardHolderRequest.BankAccount bankAccount) {
        if (bankAccount.account() != null
                && bankAccount.bankCode() != null
                && bankAccount.agency() != null) {
            return true;
        } else {
            return false;
        }
    }

    private CreditAnalysisResponse getCreditAnalysisApi(CardHolderRequest cardHolderRequest) {
        try {
            final CreditAnalysisResponse creditAnalysis = creditApi.getAnalysis(cardHolderRequest.creditAnalysisId());
            if (!creditAnalysis.clientId().equals(cardHolderRequest.clientId())) {
                throw new DivergentCreditAnalysisAndClientException("Client id does not correspond with this Credit Analysis");
            }
            if (!creditAnalysis.approved()) {
                throw new UnapprovedCreditAnalysisException("Card Holder negated due to unapproved Credit Analysis");
            }
            return creditAnalysis;
        } catch (RetryableException e) {
            throw new CreditAnalysisApiUnavailableException("Credit Analysis Api unavailable");
        } catch (FeignException e) {
            throw new CreditAnalysisNotFoundException("Credit Analysis not found");
        }
    }

}
