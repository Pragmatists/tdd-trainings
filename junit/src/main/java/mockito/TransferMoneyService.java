package mockito;

public class TransferMoneyService {

    private AccountRepository accountRepository;

    public TransferMoneyService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transfer(User payingUser, User receiver, int i) {
        
    }

    
}
