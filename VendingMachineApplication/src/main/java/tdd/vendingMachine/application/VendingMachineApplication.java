package tdd.vendingMachine.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import tdd.vendingMachine.domain.CoinDispenser;
import tdd.vendingMachine.domain.Price;
import tdd.vendingMachine.domain.PriceList;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductFeeder;
import tdd.vendingMachine.domain.ProductStorage;
import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.infrastructure.jdbc.JdbcProductStorage;

@Configuration
public class VendingMachineApplication {

    @Bean
    @Autowired
    public ProductStorage productStorage(JdbcTemplate template){
        
        ProductStorage storage = new JdbcProductStorage(template);

        storage.loadOnShelf(1, new Product("Woda mineralna"));
        storage.loadOnShelf(1, new Product("Woda mineralna"));
        storage.loadOnShelf(1, new Product("Woda mineralna"));
        storage.loadOnShelf(2, new Product("Woda mineralna"));
        storage.loadOnShelf(2, new Product("Woda mineralna"));
        storage.loadOnShelf(3, new Product("Baton czekoladowy"));
        storage.loadOnShelf(3, new Product("Baton czekoladowy"));
        storage.loadOnShelf(4, new Product("Baton kokosowy"));
        storage.loadOnShelf(5, new Product("Sok jabłkowy"));
        storage.loadOnShelf(5, new Product("Sok jabłkowy"));
        storage.loadOnShelf(5, new Product("Sok jabłkowy"));
        storage.loadOnShelf(6, new Product("Sok pomarańczowy"));
        storage.loadOnShelf(6, new Product("Sok pomarańczowy"));
        storage.loadOnShelf(6, new Product("Sok pomarańczowy"));
        storage.loadOnShelf(6, new Product("Sok pomarańczowy"));
        storage.loadOnShelf(7, new Product("Napój cola"));
        storage.loadOnShelf(7, new Product("Napój cola"));
        
        return storage;
    }

    @Bean
    @Autowired
    public VendingMachine vendingMachine(ProductStorage productStorage){
        return new VendingMachine(productStorage, new CoinDispenser(), new ProductFeeder(), new StaticPriceList());
    }
    
    public class StaticPriceList extends PriceList {
        
        @Override
        public Price priceOf(Product product) {
            return Price.of("2,50");
        }
        
    }
}

