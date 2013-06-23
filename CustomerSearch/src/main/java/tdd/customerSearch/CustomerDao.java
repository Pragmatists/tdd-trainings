package tdd.customerSearch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class CustomerDao {

    private JdbcTemplate template;

    public CustomerDao(JdbcTemplate template) {
        this.template = template;
    }
    
    public List<Customer> getCustomersByCriteria(CustomerCriteria criteria){

        String sql = "SELECT DISTINCT c.customer_num, c.name, c.active, c.registration_dt FROM customer c";
        
        if(criteria.getFirstname() != null || criteria.getLastname() != null || criteria.getMale() != null ||
                criteria.getStreet() != null || criteria.getCity() != null || criteria.getCountry() != null || criteria.getZipCode() != null){
            sql += " JOIN customer_contact cp on cp.customer_num = c.customer_num";
            sql += " JOIN contact x ON x.id = cp.contact_id";
        }
        if(criteria.getStreet() != null || criteria.getCity() != null || criteria.getCountry() != null || criteria.getZipCode() != null){
            sql += " JOIN address a on a.contact_id = x.id";
        }
        if(criteria.getSerialNumber() != null || (criteria.getType() != null && !criteria.getType().isEmpty())){
            sql += " JOIN product p on p.customer_num = c.customer_num";
        }
        
        List<String> where = new ArrayList<String>();
        
        // Contact:
        if(criteria.getFirstname() != null){
            where.add("x.firstname = '" + criteria.getFirstname() + "'");
        }
        if(criteria.getLastname() != null){
            where.add("x.lastname = '" + criteria.getLastname() + "'");
        }
        if(criteria.getMale() != null){
            where.add("x.male = '" + (criteria.getMale() ? "Y" : "N") + "'");
        }
        
        // Address:
        if(criteria.getStreet() != null){
            where.add("a.street = '" + criteria.getStreet() + "'");
        }
        if(criteria.getCity() != null){
            where.add("a.city = '" + criteria.getCity() + "'");
        }
        if(criteria.getCity() != null){
            where.add("a.country = '" + criteria.getCountry() + "'");
        }
        if(criteria.getZipCode() != null){
            where.add("a.zip_code = '" + criteria.getZipCode() + "'");
        }
        
        // Product:
        if(criteria.getSerialNumber() != null){
            where.add("p.serial_num = '" + criteria.getSerialNumber() + "'");
        }
        if(criteria.getType() != null && !criteria.getType().isEmpty()){
            
            String in = "";
            boolean first = true;
            for (ProductType type : criteria.getType()) {
                if(first){
                    first = false;
                } else{
                    in += ", ";
                }
                in += "'" + type.name() + "'";
            }
            
            where.add("p.product_type in (" + in + ")");
        }
        
        // Product Aggregate:
        if(criteria.getTotalPriceGreaterThan() != null){
            where.add("(SELECT sum(pa.price) FROM product pa WHERE pa.customer_num = c.customer_num) > " + criteria.getTotalPriceGreaterThan() + "");
        }
        
        // Customer:
        if(criteria.getNumber() != null){
            where.add("c.customer_num = '" + criteria.getNumber() + "'");
        }
        if(criteria.getName() != null){
            where.add("c.name = '" + criteria.getName() + "'");
        }
        if(criteria.getRegistrationFrom() != null){
            String registrationFrom = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getRegistrationFrom());
            where.add("c.registration_dt >= PARSEDATETIME('" + registrationFrom + "','yyyy-MM-dd')");
        }
        if(criteria.getRegistrationTo() != null){
            String registrationTo = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getRegistrationTo());
            where.add("c.registration_dt <= PARSEDATETIME('" + registrationTo + "','yyyy-MM-dd')");
        }
        if(criteria.getActive() != null){
            where.add("c.active = '" + (criteria.getActive() ? "Y" : "N") + "'");
        }
        
        if(!where.isEmpty()){
            sql += " WHERE ";
            boolean first = true;
            for (String constraint : where) {
                if(first){
                    first = false;
                } else{
                    sql += " AND ";
                }
                sql += constraint;
            }
        }
        
        System.out.println(sql);
        
        List<Customer> results = template.query(sql, new RowMapper<Customer>(){

            @Override
            public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                
                Customer customer = new Customer(rs.getString("customer_num"));
                customer.setName(rs.getString("name"));
                customer.setActive(rs.getString("active").equals("Y"));
                customer.getRegistrationDate(rs.getDate("registration_dt"));
                
                return customer;
            }
        });
        
        return results;
    }
    
}
