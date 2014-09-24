package tdd.vendingMachine.infrastructure.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tdd.vendingMachine.domain.ProductStorageContractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("jdbc-context.xml")
public class JdbcProductStorageIntegrationTest extends ProductStorageContractTest{

    @Autowired
    private JdbcTemplate template;
    
    @Before
    public void setUp() {

        storage = new JdbcProductStorage(template);
        cleanDb();
    }
    
    public void cleanDb(){
        storage.clear();
    }
    
    @Test
    public void shouldAutowireJdbcTemplate() throws Exception {

        // given:
        // when:
        // then:
        assertThat(template).isNotNull();
    }
    
}
