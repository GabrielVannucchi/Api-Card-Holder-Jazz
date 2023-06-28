package tech.jazz.apicardholder.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import tech.jazz.apicardholder.infrastructure.domain.CardHolderDomain;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface CardHolderMapper {

    CardHolderEntity from(CardHolderDomain cardHolderDomain);

    CardHolderResponse from(CardHolderEntity cardHolderEntity);
}
