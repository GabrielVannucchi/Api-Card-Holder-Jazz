package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapper;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.presentation.dto.UpdateLimitRequest;
import tech.jazz.apicardholder.presentation.dto.UpdateLimitResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.CardNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.InsufficientLimitException;

@Service
@RequiredArgsConstructor
public class PatchCardService {
    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;
    private final CardMapper cardMapper;

    public UpdateLimitResponse updateLimit(UUID cardHolderId, UUID cardId, UpdateLimitRequest updateLimitRequest) {
        final CardHolderEntity cardHolderEntity = getCardHolderOrThrowException(cardHolderId);
        CardEntity cardEntity = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("Card not found for given Id"));
        checkIfLimitIsSufficientOfThrowException(updateLimitRequest.limit(), cardHolderEntity, cardEntity);
        if (!cardEntity.getCardHolder().getCardHolderId().equals(cardHolderId)) {
            throw new DivergentCardHolderException("This card does not belong this card holder");
        }
        cardEntity = cardEntity.toBuilder()
                .limit(updateLimitRequest.limit().setScale(2))
                .build();
        cardEntity = cardRepository.save(cardEntity);

        return new UpdateLimitResponse(cardEntity.getCardId(), cardEntity.getLimit());
    }

    private void checkIfLimitIsSufficientOfThrowException(BigDecimal newLimit, CardHolderEntity cardHolder, CardEntity card) {
        final List<CardEntity> cardEntities = cardRepository
                .findByCardHolder_CardHolderIdAndCardIdNot(cardHolder.getCardHolderId(), card.getCardId());
        final BigDecimal usedLimit = cardEntities.stream()
                .map(CardEntity::getLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal remainingLimit = cardHolder.getLimit().subtract(usedLimit);
        if (newLimit.compareTo(remainingLimit) > 0) {
            throw new InsufficientLimitException(
                    String.format("Insufficient free limit to update card (%.2f) ",
                            remainingLimit.subtract(card.getLimit()).setScale(2).doubleValue())
            );
        }
    }

    private CardHolderEntity getCardHolderOrThrowException(UUID cardHolderId) {
        return cardHolderRepository.findByCardHolderId(cardHolderId).orElseThrow(
                () -> new CardHolderNotFoundException("Card Holder not found for given id")
        );
    }

}
