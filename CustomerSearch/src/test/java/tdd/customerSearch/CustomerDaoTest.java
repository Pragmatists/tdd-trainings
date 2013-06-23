package tdd.customerSearch;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jdbc-context.xml")
public class CustomerDaoTest {

    @Autowired
    private JdbcTemplate template;
    
    private CustomerDao dao;
    
    @Before
    public void setUp() {
        dao = new CustomerDao(template);
        
        template.batchUpdate(new String[]{
                "DELETE FROM customer_contact", 
                "DELETE FROM address", 
                "DELETE FROM contact", 
                "DELETE FROM product",
                "DELETE FROM customer" 
                });
    }
    
    @Test
    public void shouldFilterByActiveAndRegistration() throws Exception {

        // given:
        customer("1", "Name1", Boolean.TRUE, "2012-05-01");
        customer("2", "Name2", Boolean.FALSE, "2012-01-01");
        customer("3", "Name3", Boolean.TRUE, "2012-05-10");
        customer("4", "Name4", Boolean.FALSE, "2013-05-01");
        
        // when:
        CustomerCriteria criteria = new CustomerCriteria();
        criteria.setActive(Boolean.TRUE);
        criteria.setRegistrationFrom(date("2012-05-05"));
        
        List<Customer> result = dao.getCustomersByCriteria(criteria);
        
        // then:
        assertThat(extractProperty("name").from(result)).containsExactly("Name3");
    }

    @Test
    public void shouldFilterByAddress() throws Exception {
        
        // given:
        customer("1", "Name1", Boolean.TRUE, "2012-05-01");
        contact("1", "John", "Doe", "Wita Stwosza", "02-622", "Warszawa", "Polska");
        customer_contact("1", "1");
        
        customer("2", "Name2", Boolean.FALSE, "2012-01-01");
        contact("2", "Jane", "Doe", "Woronicza", "00-999", "Warszawa", "Polska");
        customer_contact("2", "2");
        
        customer("3", "Name3", Boolean.TRUE, "2012-05-10");
        contact("3", "Sherlock", "Holmes", "Marszałkowska", "00-001", "Warszawa", "Polska");
        customer_contact("3", "3");
        
        customer("4", "Name4", Boolean.FALSE, "2013-05-01");
        
        // when:
        CustomerCriteria criteria = new CustomerCriteria();
        criteria.setStreet("Woronicza");
        
        List<Customer> result = dao.getCustomersByCriteria(criteria);
        
        // then:
        assertThat(extractProperty("name").from(result)).containsExactly("Name2");
    }

    @Test
    public void shouldFilterByContact() throws Exception {
        
        // given:
        customer("1", "Name1", Boolean.TRUE, "2012-05-01");
        contact("1", "John", "Doe", "Wita Stwosza", "02-622", "Warszawa", "Polska");
        customer_contact("1", "1");
        
        customer("2", "Name2", Boolean.FALSE, "2012-01-01");
        contact("2", "Jane", "Doe", "Woronicza", "00-999", "Warszawa", "Polska");
        customer_contact("2", "2");
        
        customer("3", "Name3", Boolean.TRUE, "2012-05-10");
        contact("3", "Sherlock", "Holmes", "Marszałkowska", "00-001", "Warszawa", "Polska");
        customer_contact("3", "3");
        
        customer("4", "Name4", Boolean.FALSE, "2013-05-01");
        
        // when:
        CustomerCriteria criteria = new CustomerCriteria();
        criteria.setLastname("Doe");
        criteria.setActive(true);
        
        List<Customer> result = dao.getCustomersByCriteria(criteria);
        
        // then:
        assertThat(extractProperty("name").from(result)).containsExactly("Name1");
    }

    @Test
    public void shouldFilterByProduct() throws Exception {
        
        // given:
        customer("1", "Name1", Boolean.TRUE, "2012-05-01");
        product("xyz/123", "1", "A", "223");
        product("xyz/567", "1", "B", "123");
        
        customer("2", "Name2", Boolean.FALSE, "2012-01-01");
        product("abc/554", "2", "A", "654");
        product("def/245", "2", "C", "222");
        
        customer("3", "Name3", Boolean.TRUE, "2012-05-10");
        product("ghj/126", "3", "A", "776");
        product("frt/111", "3", "A", "345");
        product("tyu/155", "3", "A", "322");
        
        customer("4", "Name4", Boolean.FALSE, "2013-05-01");
        product("qwa/345", "4", "B", "664");
        
        // when:
        CustomerCriteria criteria = new CustomerCriteria();
        criteria.setType(ProductType.B, ProductType.C);
        
        List<Customer> result = dao.getCustomersByCriteria(criteria);
        
        // then:
        assertThat(result).hasSize(3);
        assertThat(extractProperty("name").from(result)).containsOnly("Name1", "Name2", "Name4");
    }
    
    @Test
    public void shouldFilterByProductAggregate() throws Exception {
        
        // given:
        customer("1", "Name1", Boolean.TRUE, "2012-05-01");
        product("xyz/123", "1", "A", "223");
        product("xyz/567", "1", "B", "123");
        
        customer("2", "Name2", Boolean.FALSE, "2012-01-01");
        product("abc/554", "2", "A", "654");
        product("def/245", "2", "C", "222");
        
        customer("3", "Name3", Boolean.TRUE, "2012-05-10");
        product("ghj/126", "3", "A", "776");
        product("frt/111", "3", "A", "345");
        product("tyu/155", "3", "A", "322");
        
        customer("4", "Name4", Boolean.FALSE, "2013-05-01");
        product("qwa/345", "4", "B", "664");
        
        // when:
        CustomerCriteria criteria = new CustomerCriteria();
        criteria.setTotalPriceGreaterThan(1000);
        
        List<Customer> result = dao.getCustomersByCriteria(criteria);
        
        // then:
        assertThat(extractProperty("name").from(result)).containsOnly("Name3");
    }
    
    // --
    
    private void contact(String id, String firstname, String lastname, String street, String zipCode, String city, String country) {

        String sql = String.format(
                "INSERT INTO contact(id, firstname, lastname) VALUES (%s, '%s', '%s')",
                id, firstname, lastname);
        template.execute(sql);
        System.out.println(sql);
        
        sql = String.format(
                "INSERT INTO address(id, street, zip_code, city, country, contact_id) VALUES (%s, '%s', '%s', '%s', '%s', %s)",
                id, street, zipCode, city, country, id);
        template.execute(sql);
        System.out.println(sql);
    }

    private void customer_contact(String num, String id) {
        
        String sql = String.format(
                "INSERT INTO customer_contact(customer_num, contact_id) VALUES ('%s', %s)",
                num, id);
        template.execute(sql);
        System.out.println(sql);
    }
    
    private void product(String serial, String customerNum, String type, String price) {
        
        String sql = String.format(
                "INSERT INTO product(serial_num, customer_num, product_type, price) VALUES ('%s', '%s', '%s', '%s')",
                serial, customerNum, type, price);
        template.execute(sql);
        System.out.println(sql);
    }

    private void customer(String num, String name, Boolean active, String date) {
        
        String sql = String.format(
                "INSERT INTO customer(customer_num, name, active, registration_dt) VALUES ('%s', '%s', '%s', PARSEDATETIME('%s', 'yyyy-MM-dd'))",
                num, name, (active ? "Y" : "N"), date);
        template.execute(sql);
        System.out.println(sql);

    }

    private Date date(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }
}
