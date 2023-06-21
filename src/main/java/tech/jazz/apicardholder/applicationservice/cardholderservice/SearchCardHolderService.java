package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.repository.BankAccountRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.BankAccountEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;

@Service
@RequiredArgsConstructor
public class SearchCardHolderService {
    private final CardHolderRepository cardHolderRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CardHolderMapper cardHolderMapper;

    //Métodos temporarios apenas para testes;
    public List<CardHolderResponse> listAll() {
        final List<CardHolderEntity> cardHolderEntities = cardHolderRepository.findAll();
        final List<CardHolderResponse> cardHolderResponses = new ArrayList<>();
        for (CardHolderEntity c:
                cardHolderEntities) {
            cardHolderResponses.add(cardHolderMapper.from(c));
        }
        return cardHolderResponses;
    }

    public List<BankAccountEntity> listAccount() {
        final List<BankAccountEntity> cardHolderEntities = bankAccountRepository.findAll();
        return cardHolderEntities;
    }
}
