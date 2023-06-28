package tech.jazz.apicardholder.infrastructure.domain;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BankAccountDomainTest {
    @Test
    void should_throw_BankAccountInvalidDataException_when_account_is_incorrect(){
        assertThrows(ConstraintViolationException.class, () -> new BankAccountDomain("1", "1234", "132"));
    }
    @Test
    void should_throw_BankAccountInvalidDataException_when_agency_is_incorrect(){
        assertThrows(ConstraintViolationException.class, () -> new BankAccountDomain("12345678-9", "33", "132"));
    }
    @Test
    void should_throw_BankAccountInvalidDataException_when_bankCode_is_incorrect(){
        assertThrows(ConstraintViolationException.class, () -> new BankAccountDomain("12345678-9", "1234", "1"));
    }
    @Test
    void should_create_object(){
        BankAccountDomain bankAccountDomain = new BankAccountDomain("12345678-9", "1234", "123");
        assertNotNull(bankAccountDomain.account());
        assertNotNull(bankAccountDomain.bankCode());
        assertNotNull(bankAccountDomain.agency());

    }
}