package mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * Odignoruj drugi test. Uruchom testy.
 * -- co moga oznaczac anotacje @Mock ? 
 * Przyjrzyj sie pierwszemu testowi (ale nie zagladaj jeszcze w bebechy...)
 * -- co moga powodowac metody "when" "thenReturn" oraz "verifyZeroInteractions"?
 * -- dlaczego userRepository nie jest nullem? Czym jest? [wlacz debug, zeby sprawdzic]
 *
 * Zaimplementuj TransferMoneyService tak, aby oba testy przechodzily.
 */

@RunWith(MockitoJUnitRunner.class)
public class TransferMoneyServiceTest {

    
    @Mock
    private AccountRepository accountRepository;

    private TransferMoneyService transferMoneyService;
    
    @Before
    public void setUp() {
        transferMoneyService = new TransferMoneyService(accountRepository);
    }
    
    @Test
    public void shouldNotTransferAnyMoney() {
        //given
        User user = aUser(1);
        
        //when
        transferMoneyService.transfer(user, user, 100);
        
        //then
        verifyZeroInteractions(accountRepository);
    }
    
    @Test
    @Ignore
    public void shouldReadUserFromDatabaseAndRegisterHimInService() {
        //given
        User payingUser = aUser(1);
        User receiver = aUser(2);
        
        //when
        transferMoneyService.transfer(payingUser, receiver, 20);
        
        //then
        verify(accountRepository).transferFrom(receiver, 20);
        verify(accountRepository).transferTo(payingUser, 20);
    }
    
    // --

    private User aUser(int i) {
        return new User(i);
    }

}
