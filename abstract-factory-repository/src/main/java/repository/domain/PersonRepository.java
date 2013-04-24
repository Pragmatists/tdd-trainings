package repository.domain;

import java.util.List;

public interface PersonRepository {

    public List<Person> loadAll();

    public void store(Person person);

    public SpecificationFactory getSpecificationFactory();

    public List<Person> findMatching(Specification specification);

}