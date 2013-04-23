package tdd.pragmatists.movielist.db;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/resources/real-services.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class HelloEntityTest {

	@Autowired
	private HelloEntityRepository helloEntityRepo;

	@Test
	public void shouldHaveNamePersisted() {
		HelloEntity helloEntity = new HelloEntity("name");

		HelloEntity saved = helloEntityRepo.saveAndFlush(helloEntity);

		HelloEntity loadedEntity = helloEntityRepo.findByName( "name").get(0);
		assertThat(loadedEntity.id).isEqualTo(saved.id);
	}


}
