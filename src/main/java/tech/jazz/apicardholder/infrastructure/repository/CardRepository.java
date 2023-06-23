package tech.jazz.apicardholder.infrastructure.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    List<CardEntity> findByCardHolder_CardHolderId(UUID cardHolderId);

}