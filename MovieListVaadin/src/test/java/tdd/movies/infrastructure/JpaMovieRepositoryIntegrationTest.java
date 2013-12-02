package tdd.movies.infrastructure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@ContextConfiguration("classpath:/jdbc-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaMovieRepositoryIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;
    
    private JpaMovieRepository repository;
    
    @Before
    public void setUp() {
        repository = new JpaMovieRepository(entityManager);
    }

    @Test
    public void firstTestGoesHere() {
        
    }

}
