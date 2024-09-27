package tech.jazz.apicardholder.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicardholder.infrastructure.repository.entity.BankAccountEntity;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {
}
