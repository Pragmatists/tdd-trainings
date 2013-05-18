package tdd.vendingMachine.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoinDispenser {

    public static class ChangeCannotBeReturnedException extends RuntimeException {

        private static final long serialVersionUID = -8805573762237022525L;

        public ChangeCannotBeReturnedException(Price change, List<Coin> availableCoins) {
            super(String.format("Can not return %s change using following coins: %s", change, availableCoins));
        }
    }

    private List<Coin> availableCoins = new ArrayList<Coin>();
    
    public void accept(Coin... coins) {
        availableCoins.addAll(Arrays.asList(coins));
    }

    public List<Coin> giveBack(Price change) {

        List<Coin> coinsToUse = new ArrayList<Coin>(availableCoins);
        
        List<Coin> usedCoins = tryToGiveOutChangeUsing(change, coinsToUse);
        
        if(usedCoins == null)
            throw new ChangeCannotBeReturnedException(change, availableCoins);
        
        for (Coin coin : usedCoins) {
            availableCoins.remove(coin);
        }
        
        return usedCoins;
    }

    private List<Coin> tryToGiveOutChangeUsing(Price change, List<Coin> coinsToUse) {

        if(change.asCents() == 0){
            return Collections.emptyList();
        }
        
        for (Coin coin : coinsToUse) {
            
            Price remainingChange = change.minus(coin.asPrice());
            if(remainingChange.asCents() < 0){
                continue;
            }
        
            List<Coin> usedCoin = tryToGiveOutChangeUsing(remainingChange, coinsWitout(coinsToUse, coin));
            if(usedCoin != null){               
                return coinsWith(coin, usedCoin);
            }
        }
        
        return null;
    }

    private List<Coin> coinsWith(Coin coin, List<Coin> usedCoin) {
        
        List<Coin> listWithoutCoin = new ArrayList<Coin>(usedCoin);
        listWithoutCoin.add(coin);
        return listWithoutCoin;
    }

    private List<Coin> coinsWitout(List<Coin> coinsToUse, Coin coin) {

        List<Coin> listWithoutCoin = new ArrayList<Coin>(coinsToUse);
        listWithoutCoin.remove(coin);
        return listWithoutCoin;
    }

    public List<Coin> availableCoins() {
        return availableCoins;
    }

}
