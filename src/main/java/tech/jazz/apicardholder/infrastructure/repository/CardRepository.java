package tech.jazz.apicardholder.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tech.jazz.apicardholder.infrastructure.repository.entity.CardEntity;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    @Transactional
    @Modifying
    @Query("update CardEntity c set c.limit = ?1 where c.cardId = ?2")
    int updateLimitByCardId(BigDecimal limit, UUID cardId);

    List<CardEntity> findByCardHolder_CardHolderId(UUID cardHolderId);

    Optional<CardEntity> findByCardId(UUID cardId);

    List<CardEntity> findByCardHolder_CardHolderIdAndCardIdNot(UUID cardHolderId, UUID cardId);



}
