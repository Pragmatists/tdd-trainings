package tdd.pragmatists.movielist.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.fest.assertions.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:real-services.xml","classpath:memoryDB.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class MovieRepositoryTest {

	@Autowired
	private MovieRepository movieRepository;

	@Test
	public void shouldHaveNamePersisted() {
		Movie movie = new Movie("name");

		Movie saved = movieRepository.saveAndFlush(movie);

		Movie loadedMovie = movieRepository.findByName( "name").get(0);
		assertThat(loadedMovie.id).isEqualTo(saved.id);
		assertThat(movieRepository.count()).isEqualTo(1);
	}


}
