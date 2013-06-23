package tdd.dependencyBreaking.customer;

public interface AuthorizationService {

    public boolean hasPermission(Long userId, String permission);

}
