package tdd.vendingMachine.domain;

import java.util.ArrayList;
import java.util.List;

import tdd.vendingMachine.domain.CoinDispenser.ChangeCannotBeReturnedException;

public class VendingMachine {

    private final ProductStorage storage;
    private final CoinDispenser coinDispenser;
    private final ProductFeeder productFeeder;
    private final PriceList priceList;

    private List<Coin> inserted = new ArrayList<Coin>();
    private int selectedShelf;
    private String display = "";

    public VendingMachine(ProductStorage storage, CoinDispenser coinDispenser, ProductFeeder productFeeder, PriceList priceList) {
        this.storage = storage;
        this.coinDispenser = coinDispenser;
        this.productFeeder = productFeeder;
        this.priceList = priceList;
    }

    public String getDisplay() {

        return display;
    }

    private void updateDisplayedPrice() {
    
        if (selectedProduct() == Product.NO_PRODUCT) {
            updateDisplay("");
            return;
        }
        updateDisplay(String.format("%s: %s PLN", selectedProduct().toString(), remainingAmmount()));
    }

    private Price remainingAmmount() {

        Price remainingPrice = priceList.priceOf(selectedProduct());
        return remainingPrice.minus(insertedAmmount());
    }

    private Price insertedAmmount() {

        Price instertedAmmount = Price.of("0,00");

        for (Coin coin : inserted) {
            instertedAmmount = instertedAmmount.plus(coin.asPrice());
        }

        return instertedAmmount;
    }

    public void select(int shelfNumber) {

        selectedShelf = shelfNumber;
        updateDisplayedPrice();
    }

    public void insert(Coin money) {

        inserted.add(money);
        
        if (fullyPaid()) {
            giveProductAndReturnChange();
        } else{
            updateDisplayedPrice();
        }
    }

    private void giveProductAndReturnChange() {

        try {
        
            coinDispenser.accept(inserted.toArray(new Coin[0]));
            if (remainingAmmount().asCents() < 0) {
                coinDispenser.giveBack(Price.fromCents(-remainingAmmount().asCents()));
            }
            storage.takeFromShelf(selectedShelf);
            productFeeder.release(selectedProduct());
            clearSelection();
            updateDisplayedPrice();
            
        } catch (ChangeCannotBeReturnedException e) {
            coinDispenser.giveBack(insertedAmmount());
            updateDisplay("No change!");
        }
        inserted.clear();
    }

    private boolean fullyPaid() {
        return remainingAmmount().asCents() <= 0;
    }


    private void clearSelection() {
        selectedShelf = -1;
    }

    private void updateDisplay(String display) {
        this.display = display;
    }

    private Product selectedProduct() {
        return storage.productOnShelf(selectedShelf);
    }

    public void cancel() {
        coinDispenser.accept(inserted.toArray(new Coin[0]));
        coinDispenser.giveBack(insertedAmmount());
        inserted.clear();
        clearSelection();
        updateDisplayedPrice();
    }
}
