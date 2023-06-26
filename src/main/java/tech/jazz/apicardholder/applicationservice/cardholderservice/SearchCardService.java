package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapper;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.presentation.dto.CardResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.CardNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCardHolderException;

@Service
@RequiredArgsConstructor
public class SearchCardService {
    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;
    private final CardMapper cardMapper;

    public List<CardResponse> listAllByCardHolder(UUID cardHolderId) {
        checkIfCardHolderExistsOrThrowException(cardHolderId);
        final List<CardEntity> cardEntities = cardRepository.findByCardHolder_CardHolderId(cardHolderId);
        return cardEntities.stream()
                .map(cardMapper::from)
                .collect(Collectors.toList());
    }

    public CardResponse findById(UUID cardHolderId, UUID cardId) {
        checkIfCardHolderExistsOrThrowException(cardHolderId);
        final CardEntity cardEntity = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("Card not found for given Id"));
        if (!cardEntity.getCardHolder().getCardHolderId().equals(cardHolderId)) {
            throw new DivergentCardHolderException("This card does not belong this card holder");
        }
        return cardMapper.from(cardEntity);
    }

    private void checkIfCardHolderExistsOrThrowException(UUID cardHolderId) {
        cardHolderRepository.findByCardHolderId(cardHolderId).orElseThrow(
                () -> new CardHolderNotFoundException("Card Holder not found for given id")
        );
    }
}
