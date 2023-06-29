package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apicardholder.infrastructure.domain.CardDomain;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapper;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;
import tech.jazz.apicardholder.presentation.dto.CardRequest;
import tech.jazz.apicardholder.presentation.dto.CardResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.InactiveCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.InsufficientLimitException;

@Service
@RequiredArgsConstructor
public class CreateCardService {
    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;
    private final CardMapper cardMapper;
    private final CardHolderMapper cardHolderMapper;

    public CardResponse createCard(UUID cardHolderId, CardRequest cardRequest) {
        if (!cardHolderId.equals(cardRequest.cardHolderId())) {
            throw new DivergentCardHolderException("Given Id doesn't match with request Id");
        }
        final CardHolderEntity cardHolderEntity = cardHolderRepository.findByCardHolderId(cardRequest.cardHolderId()).orElseThrow(
                () -> new CardHolderNotFoundException("Card Holder not found for given id")
        );
        if (cardHolderEntity.getStatusEnum().equals(StatusEnum.INACTIVE)) {
            throw new InactiveCardHolderException("Card Holder inactive");
        }
        checkIfLimitIsSufficient(cardRequest, cardHolderEntity);
        final CardDomain cardDomain = CardDomain.builder()
                .cardHolderId(cardHolderEntity.getCardHolderId())
                .limit(cardRequest.limit())
                .build();

        CardEntity cardEntity = cardMapper.from(cardDomain);
        cardEntity = cardRepository.save(cardEntity);
        return cardMapper.from(cardEntity);
    }

    private void checkIfLimitIsSufficient(CardRequest cardRequest, CardHolderEntity cardHolder) {
        final List<CardEntity> cardEntities = cardRepository.findByCardHolder_CardHolderId(cardRequest.cardHolderId());
        final BigDecimal usedLimit = cardEntities.stream()
                .map(CardEntity::getLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal remainingLimit = cardHolder.getLimit().subtract(usedLimit);
        if (cardRequest.limit().compareTo(remainingLimit) > 0) {
            throw new InsufficientLimitException(
                    String.format("Insufficient limit to create card (%.2f)",
                            remainingLimit.setScale(2).doubleValue())
            );
        }
    }





}
