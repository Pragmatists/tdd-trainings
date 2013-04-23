package repository.domain;

import java.util.UUID;

public class Person {

    private UUID id = UUID.randomUUID();
    private String firstname;
    private String lastname;
    private boolean male;
    private Integer age;
    
    private Person() {
    }

    public Person(String firstname, String lastname, boolean male, Integer age) {
        this();
        this.firstname = firstname;
        this.lastname = lastname;
        this.male = male;
        this.age = age;
    }
    
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public boolean isMale() {
        return male;
    }
    public Integer getAge() {
        return age;
    }
 
    @Override
    public boolean equals(Object obj) {
        
        if(!(obj instanceof Person)){
            return false;
        }
        
        Person other = (Person) obj;
        return this.id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
