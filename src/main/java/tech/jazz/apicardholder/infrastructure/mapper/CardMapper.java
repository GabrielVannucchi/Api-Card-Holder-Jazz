package tech.jazz.apicardholder.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.jazz.apicardholder.infrastructure.domain.CardDomain;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.presentation.dto.CardResponse;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "cardHolder.cardHolderId", source = "cardHolderId")
    CardEntity from(CardDomain cardDomain);

    CardResponse from(CardEntity cardEntity);
}
