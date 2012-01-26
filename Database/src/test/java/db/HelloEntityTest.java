package db;

import static org.fest.assertions.Assertions.*;

import org.hibernate.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.orm.hibernate3.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.transaction.*;
import org.springframework.transaction.annotation.*;

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
