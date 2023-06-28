package tech.jazz.apicardholder.infrastructure.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;
import tech.jazz.apicardholder.infrastructure.repository.util.StatusEnum;

public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
    CardHolderEntity findFirstByClientId(UUID clientId);

    List<CardHolderEntity> findByStatusEnum(StatusEnum statusEnum);

}
