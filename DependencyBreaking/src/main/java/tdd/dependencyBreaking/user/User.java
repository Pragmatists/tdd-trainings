package tdd.dependencyBreaking.user;

public class User{

    private long deactivatedAt;
    private boolean active = true;
    
    public void deactivate(){
        
        this.active = false;
        this.deactivatedAt = System.currentTimeMillis();
        
        // ...
    }

    public long getDeactivatedAt() {
        return deactivatedAt;
    }

    public boolean isActive() {
        return active;
    }
    
}