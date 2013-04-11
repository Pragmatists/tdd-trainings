package mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

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
    private UserRepository userRepository;
    
    @Mock
    private AccountRepository accountRepository;

    private TransferMoneyService transferMoneyService;
    
    @Before
    public void setUp() {
        transferMoneyService = new TransferMoneyService(userRepository, accountRepository);
    }
    
    @Test
    public void shouldNotTransferAnyMoney() {
        //given
        User user = aUser(1);
        when(userRepository.fetchBy(aUserReference(1))).thenReturn(user);
        
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
        givenUsersInRepositoryAre(payingUser, receiver);
        
        //when
        transferMoneyService.transfer(payingUser, receiver, 20);
        
        //then
        verify(accountRepository).decrease(receiver, 20);
        verify(accountRepository).increase(payingUser, -20);
        
    }
    
    // --

    private void givenUsersInRepositoryAre(User ...users) {
        for (User user : users) {
            when(userRepository.fetchBy(aUserReference(user.getId()))).thenReturn(user);
        }
    }


    private User aUser(int i) {
        return new User(i);
    }



    private UserReference aUserReference(int i) {
        return new UserReference(i);
    }
    
}
