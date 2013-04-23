package repository.infrastructure.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.Before;

import repository.domain.Person;
import repository.infrastructure.transients.PersonRepositoryContractTest;

public class HibernatePersonRepositoryTest extends PersonRepositoryContractTest {

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() {

        buildSessionFactory();

        repository = new HibernatePersonRepository(sessionFactory){
            
            @Override
            public void store(Person person) {
                super.store(person);
                detachFromSession(person);
            }
        };
        
        initializeSession();
    }
    
    @After
    public void tearDown(){
        cleanupSession();
    }
    
    // --
    
    private void detachFromSession(Object entity) {
        session.flush();
        session.evict(entity);
    }

    private void buildSessionFactory() {

        Configuration configuration = prepareTestConfiguration();
        sessionFactory = configuration.buildSessionFactory();
    }

    private Configuration prepareTestConfiguration() {

        Configuration configuration = new Configuration();

        configuration.addResource("repository/infrastructure/hibernate/Person.hbm.xml");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        configuration.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:abstract-factory-repository");
        configuration.setProperty("hibernate.connection.username", "sa");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        return configuration;
    }

    private void initializeSession() {
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();
    }

    private void cleanupSession() {
        session.getTransaction().rollback();
        session = null;
    }
}
