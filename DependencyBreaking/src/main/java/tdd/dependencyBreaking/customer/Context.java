package tdd.dependencyBreaking.customer;

public class Context {

    private static User currentUser;

    public static User currentUser() {
        return currentUser;
    }

}
