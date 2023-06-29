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

@Service
@RequiredArgsConstructor
public class SearchCardService {
    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;
    private final CardMapper cardMapper;

    public List<CardResponse> listAllByCardHolder(UUID cardHolderId) {
        cardHolderRepository.findByCardHolderId(cardHolderId).orElseThrow(
                () -> new CardHolderNotFoundException("Card Holder not found for given id")
        );
        final List<CardEntity> cardEntities = cardRepository.findByCardHolder_CardHolderId(cardHolderId);
        return cardEntities.stream()
                .map(cardMapper::from)
                .collect(Collectors.toList());
    }
}
