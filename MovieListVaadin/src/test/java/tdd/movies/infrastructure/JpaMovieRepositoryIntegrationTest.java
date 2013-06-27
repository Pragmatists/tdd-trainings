package tdd.movies.infrastructure;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.groups.Properties.extractProperty;
import static org.junit.Assert.*;
import static tdd.movies.infrastructure.MovieBuilder.aMovie;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.fest.assertions.api.Assertions;
import org.fest.assertions.groups.Properties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tdd.movies.domain.Movie;

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
	public void shouldPersistAndLoadMovie() throws Exception {
		Movie movie = aMovie().titled("Avatar").persist(entityManager);
		
		List<Movie> movies = repository.loadAll();
		
		assertThat(movies).containsExactly(movie);
		assertThat(extractProperty("title").from(movies)).containsExactly("Avatar");
	}


}
