package repository.infrastructure.hibernate;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import repository.domain.Person;
import repository.domain.PersonRepository;
import repository.domain.Specification;
import repository.domain.SpecificationFactory;

public class HibernatePersonRepository implements PersonRepository{

    private final SessionFactory sessionFactory;

    public HibernatePersonRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public List<Person> loadAll() {
        List<Person> list = currentSession().createCriteria(Person.class).list();
        return Collections.unmodifiableList(list);
    }

    public void store(Person person) {
        currentSession().save(person);
    }

    public SpecificationFactory getSpecificationFactory() {

        return null;
    }

    public List<Person> findMatching(Specification specification) {

        return null;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

}
