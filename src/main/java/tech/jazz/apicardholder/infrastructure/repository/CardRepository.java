package tech.jazz.apicardholder.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    List<CardEntity> findByCardHolder_CardHolderId(UUID cardHolderId);

    Optional<CardEntity> findByCardId(UUID cardId);

    List<CardEntity> findByCardHolder_CardHolderIdAndCardIdNot(UUID cardHolderId, UUID cardId);



}
