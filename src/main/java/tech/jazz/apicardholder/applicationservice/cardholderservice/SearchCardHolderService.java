package tech.jazz.apicardholder.applicationservice.cardholderservice;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;

@Service
@RequiredArgsConstructor
public class SearchCardHolderService {
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderMapper cardHolderMapper;

    public List<CardHolderResponse> listAll() {
        final List<CardHolderEntity> cardHolderEntities = cardHolderRepository.findAll();
        return cardHolderEntities.stream()
                .map(cardHolderMapper::from)
                .collect(Collectors.toList());
    }
}
