package tdd.vendingMachine.infrastructure.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductStorage;

public class JdbcProductStorage implements ProductStorage{

    private final JdbcTemplate template;

    public JdbcProductStorage(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void loadOnShelf(int shelfNumber, Product productToLoad) {
    
        Product product = productOnShelf(shelfNumber);
        
        String sql = null;
        if(product != Product.NO_PRODUCT){
            sql = String.format("update PRODUCT_SHELF set ITEMS=ITEMS+1 where SHELF_NUMBER='%s'", shelfNumber);
        } else{
            sql = String.format("insert into PRODUCT_SHELF(SHELF_NUMBER, PRODUCT_NAME, ITEMS) values ('%s','%s', 1)", shelfNumber, productToLoad);
        }
        
        template.execute(sql);
    }
    
    @Override
    public Product productOnShelf(int shelfNumber) {

        String sql = String.format("select * from PRODUCT_SHELF where SHELF_NUMBER='%s'", shelfNumber);
        
        List<Product> products = template.query(sql, new RowMapper<Product>(){
            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Product(rs.getString("PRODUCT_NAME"));
            }
        });
        
        return products.isEmpty() ? Product.NO_PRODUCT : products.get(0);
    }

    @Override
    public Product takeFromShelf(int shelfNumber) {

        Product product = productOnShelf(shelfNumber);
        
        String sql = String.format("update PRODUCT_SHELF set ITEMS=ITEMS-1 where SHELF_NUMBER='%s'", shelfNumber);
        template.execute(sql);
        
        String clearSql = "delete from PRODUCT_SHELF where ITEMS=0";
        template.execute(clearSql);
        
        return product;
    }

    @Override
    public void clear() {
        template.execute("delete from PRODUCT_SHELF");
    }

    @Override
    public int itemsOnShelf(int shelfNumber) {
        
        String sql = String.format("select sum(ITEMS) from PRODUCT_SHELF where SHELF_NUMBER='%s'", shelfNumber);
        return template.queryForInt(sql);
    }
    
}
