package tdd.dependencyBreaking.customer;

public class User {

    private Long id;
    
    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
