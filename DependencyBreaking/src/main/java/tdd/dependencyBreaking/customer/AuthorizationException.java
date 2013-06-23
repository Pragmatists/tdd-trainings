package tdd.dependencyBreaking.customer;

public class AuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 7274317367622581411L;

    public AuthorizationException(String missingPermission) {
        super("User have to permission '" + missingPermission + "' to complete this action!");
    }
    
}
