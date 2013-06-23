package tdd.dependencyBreaking.authentication;

public class AuthenticationException extends Exception {

    private static final long serialVersionUID = -1815407670106144619L;

    public AuthenticationException(String message) {
        super(message);
    }
    
}
