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
