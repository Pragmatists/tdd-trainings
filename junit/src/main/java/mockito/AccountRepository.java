package mockito;

public interface AccountRepository {

    void decrease(User receiver, int amount);

    void increase(User payingUser, int amount);

}
