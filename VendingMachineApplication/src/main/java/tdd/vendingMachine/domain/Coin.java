package tdd.vendingMachine.domain;

public enum Coin {
    
    FIVE_DOLLAR(Price.of("5,00")), 
    TWO_DOLLAR(Price.of("2,00")), 
    ONE_DOLLAR(Price.of("1,00")), 
    FIFTY_CENTS(Price.of("0,50")), 
    TWENTY_CENTS(Price.of("0,20")), 
    TEN_CENTS(Price.of("0,10"));

    private final Price price;

    private Coin(Price price) {
        this.price = price;
    }
    
    public Price asPrice() {
        return price;
    }
}
