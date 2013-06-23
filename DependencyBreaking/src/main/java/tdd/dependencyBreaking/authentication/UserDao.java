package tdd.dependencyBreaking.authentication;


public interface UserDao {

    public User findByLoginAndPass(String login, String password);

}
