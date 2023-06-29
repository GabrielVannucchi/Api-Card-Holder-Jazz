package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.StatusOutOfFormatException;

@Service
@RequiredArgsConstructor
public class SearchCardHolderService {
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderMapper cardHolderMapper;

    public List<CardHolderResponse> listAll(String status) {
        if (Objects.isNull(status)) {
            return cardHolderRepository.findAll().stream()
                    .map(cardHolderMapper::from)
                    .collect(Collectors.toList());
        } else {
            status = status.toUpperCase();
            switch (status) {
            case "ACTIVE":
            case "INACTIVE":
                final List<CardHolderEntity> cardHolderEntities =
                        cardHolderRepository.findByStatusEnum(StatusEnum.valueOf(status));
                return cardHolderEntities.stream()
                        .map(cardHolderMapper::from)
                        .collect(Collectors.toList());
            default:
                throw new StatusOutOfFormatException("Incorrect status. Only accept ACTIVE or INACTIVE");
            }
        }
    }

    public CardHolderResponse findCardHolder(UUID id) {
        final CardHolderEntity cardHolderEntity = cardHolderRepository.findByCardHolderId(id).orElseThrow(() ->
                new CardHolderNotFoundException("Card Holder not found for given Id")
        );
        return cardHolderMapper.from(cardHolderEntity);
    }
}
