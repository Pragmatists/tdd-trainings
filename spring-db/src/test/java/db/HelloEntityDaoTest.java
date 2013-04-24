package db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:real-services.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class HelloEntityDaoTest {

    @Autowired
    private HelloEntityDao helloEntityDao;

    @Test
    public void persistedWithJdbcTemplate() {
        HelloEntity helloEntity = new HelloEntity().id("hello");
        helloEntityDao.persist(helloEntity);
        HelloEntity loaded = helloEntityDao.load("hello");
        assertThat(loaded.getId()).isEqualTo(helloEntity.getId());
    }

}
