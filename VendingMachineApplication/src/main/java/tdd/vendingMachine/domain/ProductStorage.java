package tdd.vendingMachine.domain;

public interface ProductStorage {

    public Product productOnShelf(int shelfNumber);
    public int itemsOnShelf(int shelfNumber);

    public void loadOnShelf(int shelfNumber, Product productToLoad);
    public Product takeFromShelf(int shelfNumber);
    
    public void clear();

}