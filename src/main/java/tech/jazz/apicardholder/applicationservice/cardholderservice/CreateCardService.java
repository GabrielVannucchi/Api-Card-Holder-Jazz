package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.mapper.CardMapper;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.presentation.dto.CardRequest;
import tech.jazz.apicardholder.presentation.dto.CardResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCardHolderException;
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
            throw new DivergentCardHolderException("Giver Id doesn't match with request Id");
        }
        final CardHolderEntity cardHolderEntity = cardHolderRepository.findByCardHolderId(cardRequest.cardHolderId()).orElseThrow(
                () -> new CardHolderNotFoundException("Card Holder not found for given id")
        );
        checkIfLimitIsSufficientOfThrowException(cardRequest, cardHolderEntity);
        final CardEntity cardEntity = CardEntity.builder()
                .cvv(555)
                .cardNumber("1234 1234 1234 1234")
                .dueDate(LocalDate.now())
                .cardHolder(cardHolderEntity)
                .limit(cardRequest.limit())
                .build();
        return cardMapper.from(cardRepository.save(cardEntity));
    }

    private void checkIfLimitIsSufficientOfThrowException(CardRequest cardRequest, CardHolderEntity cardHolder) {
        final List<CardEntity> cardEntities = cardRepository.findByCardHolder_CardHolderId(cardRequest.cardHolderId());
        final BigDecimal usedLimit = cardEntities.stream()
                .map(CardEntity::getLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal remainingLimit = cardHolder.getLimit().subtract(usedLimit);
        if (cardRequest.limit().compareTo(remainingLimit) > 0) {
            throw new InsufficientLimitException(
                    String.format("Insufficient limit to create card (%.2f)",
                            remainingLimit.setScale(2).doubleValue()
                            )
            );
        }
    }

}
