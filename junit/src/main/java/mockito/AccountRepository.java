package mockito;

public interface AccountRepository {

    void transferTo(User receiver, int amount);

    void transferFrom(User payingUser, int amount);

}
