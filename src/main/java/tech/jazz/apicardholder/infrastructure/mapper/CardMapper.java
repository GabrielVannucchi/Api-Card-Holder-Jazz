package tech.jazz.apicardholder.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import tech.jazz.apicardholder.infrastructure.domain.CardDomain;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;
import tech.jazz.apicardholder.presentation.dto.CardResponse;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CardMapper {
    CardEntity from(CardDomain cardDomain);

    CardResponse from(CardEntity cardEntity);
}
