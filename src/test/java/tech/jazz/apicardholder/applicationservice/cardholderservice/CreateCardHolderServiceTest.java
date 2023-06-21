package tech.jazz.apicardholder.applicationservice.cardholderservice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jazz.apicardholder.applicationservice.creditapi.CreditApi;
import tech.jazz.apicardholder.infrastructure.mapper.CardHolderMapper;
import tech.jazz.apicardholder.infrastructure.repository.BankAccountRepository;
import tech.jazz.apicardholder.infrastructure.repository.CardHolderRepository;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateCardHolderServiceTest {
    @Mock
    CreditApi creditApi;
    @Mock
    CardHolderRepository cardHolderRepository;
    @Mock
    BankAccountRepository bankAccountRepository;
    @Spy
    CardHolderMapper cardHolderMapper;
    @InjectMocks
    CreateCardHolderService createCardHolderService;


}