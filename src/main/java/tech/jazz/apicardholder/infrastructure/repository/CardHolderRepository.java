package tech.jazz.apicardholder.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardHolderEntity;

public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
    CardHolderEntity findFirstByClientId(UUID clientId);

}
