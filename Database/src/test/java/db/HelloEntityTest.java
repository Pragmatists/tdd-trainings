package db;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/resources/real-services.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class HelloEntityTest {

	@Autowired
	private SessionFactory sessionFactory;
	private HibernateTemplate hibernateTemplate;

	@Before
	public void setupTemplate() {
		hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	@Test
	public void shouldHaveNamePersisted() {
		HelloEntity helloEntity = new HelloEntity().id("hello");

		hibernateTemplate.persist(helloEntity);

		flushAndClear();
		HelloEntity loadedEntity = (HelloEntity) hibernateTemplate.load(
		        HelloEntity.class, "hello");
		assertThat(loadedEntity.getId()).isEqualTo("hello");
	}

	private void flushAndClear() {
		hibernateTemplate.flush();
		hibernateTemplate.clear();
	}

}
