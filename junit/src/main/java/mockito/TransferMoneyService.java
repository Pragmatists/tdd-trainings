package mockito;

public class TransferMoneyService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;

    public TransferMoneyService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public void transfer(User payingUser, User receiver, int i) {
        
    }

    
}
