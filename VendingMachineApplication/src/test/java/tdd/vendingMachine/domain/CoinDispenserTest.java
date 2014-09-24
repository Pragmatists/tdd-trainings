package tdd.vendingMachine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import tdd.vendingMachine.domain.CoinDispenser.ChangeCannotBeReturnedException;

public class CoinDispenserTest {

    private CoinDispenser coinDispenser;

    @Before
    public void setUp() {

        coinDispenser = new CoinDispenser();
    }
    
    @Test
    public void shouldBeEmptyAtStart() throws Exception {
        
        // given:
        
        // when:
        List<Coin> coins = coinDispenser.availableCoins();
        
        // then:
        assertThat(coins).isEmpty();
    }

    @Test
    public void shouldContainSingleCoin() throws Exception {
        
        // given:
        coinDispenser.accept(Coin.ONE_DOLLAR);
        
        // when:
        List<Coin> coins = coinDispenser.availableCoins();
        
        // then:
        assertThat(coins).containsExactly(Coin.ONE_DOLLAR);
    }
    
    @Test
    public void shouldContainMultipeCoin() throws Exception {
        
        // given:
        coinDispenser.accept(Coin.ONE_DOLLAR, Coin.FIFTY_CENTS, Coin.TEN_CENTS);
        
        // when:
        List<Coin> coins = coinDispenser.availableCoins();
        
        // then:
        assertThat(coins).containsExactly(Coin.ONE_DOLLAR, Coin.FIFTY_CENTS, Coin.TEN_CENTS);
    }
    
    @Test
    public void shouldGiveBackSingleCoin() throws Exception {

        // given:
        coinDispenser.accept(Coin.ONE_DOLLAR);
        
        // when:
        List<Coin> coins = coinDispenser.giveBack(Price.of("1,00"));
        
        // then:
        assertThat(coins).containsExactly(Coin.ONE_DOLLAR);
    }

    @Test
    public void shouldRemoveReturenedCoinsFromAvailable() throws Exception {
        
        // given:
        coinDispenser.accept(Coin.ONE_DOLLAR);
        
        // when:
        coinDispenser.giveBack(Price.of("1,00"));
        
        // then:
        assertThat(coinDispenser.availableCoins()).isEmpty();
    }
    
    @Test
    public void shouldGiveBackSingleOutOfTwo() throws Exception {
        
        // given:
        coinDispenser.accept(Coin.FIFTY_CENTS, Coin.ONE_DOLLAR);
        
        // when:
        List<Coin> coins = coinDispenser.giveBack(Price.of("1,00"));
        
        // then:
        assertThat(coins).containsExactly(Coin.ONE_DOLLAR);
    }

    @Test
    public void shouldGiveBackTwoCoins() throws Exception {
        
        // given:
        coinDispenser.accept(Coin.FIFTY_CENTS, Coin.FIFTY_CENTS);
        
        // when:
        List<Coin> coins = coinDispenser.giveBack(Price.of("1,00"));
        
        // then:
        assertThat(coins).containsExactly(Coin.FIFTY_CENTS, Coin.FIFTY_CENTS);
    }

    @Test
    public void shouldGiveBackCoinComplexCase() throws Exception {
        
        // given:
        coinDispenser.accept(
                Coin.FIFTY_CENTS, Coin.FIFTY_CENTS, Coin.ONE_DOLLAR, Coin.TWO_DOLLAR, 
                Coin.TWENTY_CENTS, Coin.TWENTY_CENTS, Coin.TEN_CENTS, Coin.TWO_DOLLAR);
        
        // when:
        List<Coin> coins = coinDispenser.giveBack(Price.of("4,80"));
        
        // then:
        assertThat(sumOf(coins)).isEqualTo(Price.of("4,80"));
    }
    
    @Test
    public void shouldFailIfChangeCannotBeReturned() throws Exception {
        
        // given:
        coinDispenser.accept(Coin.FIFTY_CENTS, Coin.ONE_DOLLAR);
        
        try{
            // when:
            coinDispenser.giveBack(Price.of("0,70"));
            Assertions.failBecauseExceptionWasNotThrown(ChangeCannotBeReturnedException.class);
            
        } catch(Exception e){
            // then:
            assertThat(e)
                .isInstanceOf(ChangeCannotBeReturnedException.class)
                .hasMessage("Can not return 0,70 change using following coins: [FIFTY_CENTS, ONE_DOLLAR]");
        }
    }

    @Test
    public void shouldNotRemoveCoinsIfChangeCannotBeReturned() throws Exception {
        
        // given:
        coinDispenser.accept(Coin.FIFTY_CENTS, Coin.ONE_DOLLAR);
        
        try{
            // when:
            coinDispenser.giveBack(Price.of("0,70"));
            Assertions.failBecauseExceptionWasNotThrown(ChangeCannotBeReturnedException.class);
            
        } catch(Exception e){
            // then:
            assertThat(coinDispenser.availableCoins()).containsExactly(Coin.FIFTY_CENTS, Coin.ONE_DOLLAR);
        }
    }

    // --
    
    private Price sumOf(List<Coin> coins) {
        Price sum = Price.of("0,00");
        for (Coin coin : coins) {
            sum = sum.plus(coin.asPrice());
        }
        return sum;
    }
}

