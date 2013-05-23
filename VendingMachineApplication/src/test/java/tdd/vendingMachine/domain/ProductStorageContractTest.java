package tdd.vendingMachine.domain;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public abstract class ProductStorageContractTest {

    protected ProductStorage storage;
    
    @Test
    public void shouldBeEmptyJustAfterCreation() throws Exception {

        // given:
        // when:
        Product product = storage.productOnShelf(1);

        // then:
        assertThat(product).isEqualTo(Product.NO_PRODUCT);
    }
    
    @Test
    public void shouldNotBeEmptyAfterLoadingProductOnShelf() throws Exception {

        // given:
        storage.loadOnShelf(1, aProduct("Chocolate Bar"));
        
        // when:
        Product product = storage.productOnShelf(1);

        // then:
        assertThat(product).isEqualTo(aProduct("Chocolate Bar"));
    }

    @Test
    public void shouldBeEmptyAfterLoadingProductOnOtherShelf() throws Exception {

        // given:
        storage.loadOnShelf(1, aProduct("Chocolate Bar"));
        
        // when:
        Product product = storage.productOnShelf(2);

        // then:
        assertThat(product).isEqualTo(Product.NO_PRODUCT);
    }

    @Test
    public void shouldTakeProductFromShelf() throws Exception {

        // given:
        storage.loadOnShelf(1, aProduct("Chocolate Bar"));
        
        // when:
        Product product = storage.takeFromShelf(1);
        
        // then:
        assertThat(product).isEqualTo(aProduct("Chocolate Bar"));
    }
    
    @Test
    public void shouldRemoveProductFromShelfAfterTake() throws Exception {
        
        // given:
        storage.loadOnShelf(1, aProduct("Chocolate Bar"));
        
        // when:
        storage.takeFromShelf(1);
        
        // then:
        assertThat(storage.productOnShelf(1)).isEqualTo(Product.NO_PRODUCT);
    }
    
    @Test
    public void shouldLoadMultipleProductsOnShelf() throws Exception {
        
        // given:
        storage.loadOnShelf(1, aProduct("Chocolate Bar"));
        storage.loadOnShelf(1, aProduct("Chocolate Bar"));
        
        // when:
        Integer items = storage.itemsOnShelf(1);
        
        // then:
        assertThat(items).isEqualTo(2);
    }
    
    @Test
    public void shouldClearStorage() throws Exception {

        // given:
        storage.loadOnShelf(1, aProduct("Chocolate Bar"));
        storage.loadOnShelf(1, aProduct("Chocolate Bar"));
        storage.loadOnShelf(2, aProduct("Mineral Water"));
        
        // when:
        storage.clear();
        
        // then:
        assertThat(storage.productOnShelf(1)).isEqualTo(Product.NO_PRODUCT);
        assertThat(storage.productOnShelf(2)).isEqualTo(Product.NO_PRODUCT);
    }
    
    // --
    
    private Product aProduct(String name) {
        return new Product(name);
    }
}

