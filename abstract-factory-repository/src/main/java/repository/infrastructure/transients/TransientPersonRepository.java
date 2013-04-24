package repository.infrastructure.transients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repository.domain.Person;
import repository.domain.PersonRepository;
import repository.domain.Specification;

public class TransientPersonRepository implements PersonRepository {

    private List<Person> store = new ArrayList<Person>();

    public List<Person> loadAll() {
        return Collections.unmodifiableList(store);
    }

    public void store(Person person) {
        store.add(person);
    }

    public TransientSpecificationFactory getSpecificationFactory() {
        return new TransientSpecificationFactory();
    }

    public List<Person> findMatching(Specification specification) {
        
        List<Person> matching = new ArrayList<Person>();

        for (Person person : loadAll()) {
            if(specification.isSatisfiedBy(person))
                matching.add(person);
        }
        
        return Collections.unmodifiableList(matching);
    }

}
