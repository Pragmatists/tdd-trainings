package tdd.vendingMachine.infrastructure.transients;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;

import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductStorage;

public class TransientProductStorage implements ProductStorage {

    private Map<Integer, Shelf> shelfs = new HashMap<Integer, Shelf>();

    @Override
    public Product productOnShelf(int shelfNumber) {
    
        Shelf shelf = shelfs.get(shelfNumber);
        return shelf == null ? Product.NO_PRODUCT : shelf.product();
    }

    @Override
    public void loadOnShelf(int shelfNumber, Product productToLoad) {
        
        Shelf shelf = shelfs.get(shelfNumber);
        if(shelf == null){
            shelf = new Shelf(productToLoad);
            shelfs.put(shelfNumber, shelf);
        }
        shelf.loadOne();
    }

    public void clear() {
        shelfs.clear();
    }

    @Override
    public Product takeFromShelf(int shelfNumber) {

        Shelf shelf = shelfs.get(shelfNumber);
        Product product = shelf.take();
        
        if(shelf.isEmpty()){
            shelfs.remove(shelfNumber);
        }
        
        return product;
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY, value=JsonMethod.FIELD)
    private class Shelf{
        
        private Product product;
        private int items = 0;
        
        public Shelf(Product product) {
            this.product = product;
        }
        
        public void loadOne(){
            items++;
        }
        
        public Product product(){
            return product;
        }
        
        public Product take(){
            items--;
            return product();
        }
        
        public boolean isEmpty(){
            return items == 0;
        }
    }

    @Override
    public int itemsOnShelf(int shelfNumber) {
        return shelfs.get(shelfNumber).items;
    }
}