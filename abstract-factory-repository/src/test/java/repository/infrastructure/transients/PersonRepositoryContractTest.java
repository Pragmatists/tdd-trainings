package repository.infrastructure.transients;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import repository.domain.Person;
import repository.domain.PersonRepository;
import repository.domain.Specification;
import repository.domain.SpecificationFactory;

public abstract class PersonRepositoryContractTest {

    protected PersonRepository repository;

    @Test
    public void shouldNewlyCreatedRepositoryBeEmpty() throws Exception {
    
        // given:
        
        // when:
        List<Person> persons =  repository.loadAll();
        
        // then:
        assertThat(persons).isEmpty();
    }

    @Test
    public void shouldStoreAndLoadPerson() throws Exception {
    
        // given:
        Person person = aPerson();
    
        // when:
        repository.store(person);
        List<Person> persons = repository.loadAll();
        
        // then:
        assertThat(persons).containsOnly(person);
    }

    @Test
    public void shouldStoreAndLoadMultiplePersons() throws Exception {
    
        // given:
        Person person1 = aPerson();
        Person person2 = aPerson();
    
        // when:
        repository.store(person1);
        repository.store(person2);
        List<Person> persons = repository.loadAll();
        
        // then:
        assertThat(persons).hasSize(2);
        assertThat(persons).containsOnly(person1, person2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnedAllListBeImmutable() throws Exception {
    
        // given:
        
        // when:
        repository.loadAll().add(aPerson());
        
        // then:
    }

    @Test
    public void shouldFindAllByEmptySpecification() throws Exception {
    
        // given:
        Person person1 = persisted(aPerson());
        Person person2 = persisted(aPerson());
        
        // when:
        SpecificationFactory factory = repository.getSpecificationFactory();
        List<Person> persons = repository.findMatching(factory.any());
        
        // then:
        assertThat(persons).containsOnly(person1, person2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldFindListBeImmutable() throws Exception {
    
        // given:
    
        // when:
        SpecificationFactory factory = repository.getSpecificationFactory();
        repository.findMatching(factory.any()).add(aPerson());
        
        // then:
    }

    @Test
    public void shouldFindByFirstName() throws Exception {
    
        // given:
        Person jack = persisted(aPersonWithFirstname("Jack"));
        Person john = persisted(aPersonWithFirstname("John"));
        
        // when:
        SpecificationFactory factory = repository.getSpecificationFactory();
        List<Person> persons = repository.findMatching(factory.equalTo("firstname", "John"));
        
        // then:
        assertThat(persons).containsOnly(john);
    }

    @Test
    public void shouldFindByLastName() throws Exception {
    
        // given:
        Person jack = persisted(aPersonWithName("Jack", "Sparrow"));
        Person john = persisted(aPersonWithName("John", "Doe"));
        Person jane = persisted(aPersonWithName("Jane", "Doe"));
        
        // when:
        SpecificationFactory factory = repository.getSpecificationFactory();
        List<Person> persons = repository.findMatching(factory.equalTo("lastname", "Doe"));
        
        // then:
        assertThat(persons).containsOnly(john, jane);
    }

    @Test
    public void shouldFindByAge() throws Exception {
    
        // given:
        Person young = persisted(aPersonOfAge(5));
        Person old = persisted(aPersonOfAge(99));
        
        // when:
        SpecificationFactory factory = repository.getSpecificationFactory();
        List<Person> persons = repository.findMatching(factory.greaterThan("age", 18));
        
        // then:
        assertThat(persons).containsOnly(old);
    }

    @Test
    public void shouldFindByCombinedSpecificationAnd() throws Exception {
    
        // given:
        Person tooYoung = persisted(aFemaleOfAge(16));
        Person notThisGender = persisted(aMaleOfAge(60));
        Person notThisGenderAndTooYoung = persisted(aMaleOfAge(10));
        Person perfectMatch = persisted(aFemaleOfAge(24));
        
        // when:
        SpecificationFactory factory = repository.getSpecificationFactory();
        
        Specification adult = factory.greaterThan("age", 18);
        Specification female = factory.equalTo("male", false);
        Specification wifeSpecification = factory.and(female, adult);
        
        List<Person> persons = repository.findMatching(wifeSpecification);
        
        // then:
        assertThat(persons).containsOnly(perfectMatch);
    }

    // --
    
    private Person aPersonWithName(String firstName, String lastName) {
        return new Person(firstName, lastName, true, 44);
    }

    private Person persisted(Person person) {
        repository.store(person);
        return person;
    }

    private Person aPerson() {
        return new Person("John", "Doe", true, 44);
    }

    private Person aPersonWithFirstname(String firstName) {
        return new Person(firstName, "Doe", true, 44);
    }

    private Person aPersonOfAge(int age) {
        return new Person("John", "Doe", true, age);
    }

    private Person aFemaleOfAge(int age) {
        return new Person("Jane", "Doe", false, age);
    }

    private Person aMaleOfAge(int age) {
        return new Person("John", "Doe", true, age);
    }

}