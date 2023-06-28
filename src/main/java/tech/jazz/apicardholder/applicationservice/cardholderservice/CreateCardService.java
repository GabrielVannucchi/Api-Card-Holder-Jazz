package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
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
        checkIfLimitIsSufficient(cardRequest, cardHolderEntity);
        CardEntity cardEntity = CardEntity.builder()
                .cvv(generateCvv())
                .cardNumber(generateCardNumber())
                .dueDate(LocalDate.now().plusYears(5).plusMonths(1))
                .cardHolder(cardHolderEntity)
                .limit(cardRequest.limit())
                .build();
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

    private String generateCardNumber() {
        final List<Integer> cardNumber = new ArrayList<>();
        cardNumber.add(4);
        for (int i = 0; i < 14; i++) {
            cardNumber.add(ThreadLocalRandom.current().nextInt(10));
        }
        Integer sum = 0;
        Integer verifier = 0;
        for (int i = 0; i < cardNumber.size(); i++) {
            if (i % 2 == 0) {
                sum += cardNumber.get(i);
            } else {
                Integer aux = cardNumber.get(i) * 2;
                if (aux > 9) {
                    aux = cardNumber.get(i) - 9;
                }
                sum += aux;
            }

        }
        verifier = sum;
        while (verifier > 10) {
            verifier -= 10;
        }
        verifier = 10 - verifier;
        String cardNumberFinal = "";
        Integer cont = 0;
        for (Integer i:
                cardNumber) {
            cardNumberFinal += i;
            cont++;
            if (cont == 4) {
                cont = 0;
                cardNumberFinal += " ";
            }
        }
        cardNumberFinal += verifier;
        return cardNumberFinal;
    }

    private Integer generateCvv() {
        return ThreadLocalRandom.current().nextInt(1000);
    }

}
