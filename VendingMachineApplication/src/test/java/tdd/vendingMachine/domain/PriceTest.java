package tdd.vendingMachine.domain;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class PriceTest {

    @Test
    public void shouldCreateValidPrice() throws Exception {

        // given:
        // when:
        Price price = Price.of("234,23");

        // then:
        assertThat(price).isNotNull();
    }
    
    @Test
    public void shouldTwoPricesBeEqual() throws Exception {

        // given:
        // when:
        Price price = Price.of("234,23");
        // then:
        assertThat(price).isEqualTo(Price.of("234,23"));
        
    }
    
    @Test
    public void shouldPriceHaveInternalRepresentationInCents() throws Exception {

        // given:
        // when:
        Price price = Price.of("234,23");
        // then:
        assertThat(price.asCents()).isEqualTo(23423);
        
    }

    @Test
    public void shouldCreatePriceFromCents() throws Exception {

        // given:
        // when:
        Price price = Price.fromCents(23423);
        // then:
        assertThat(price.asCents()).isEqualTo(23423);
        
    }

    @Test
    public void shouldHaveProperToString() throws Exception {

        // given:
        Price price = Price.of("994,00");
        // when:
        String stringReprestentation = price.toString();
        // then:
        assertThat(stringReprestentation).isEqualTo("994,00");
    }
    
    @Test
    public void shouldTwoPricesHaveSameHashCode() throws Exception {

        // given:
        // when:
        Price price = Price.of("234,23");
        // then:
        assertThat(price.hashCode()).isEqualTo(Price.of("234,23").hashCode());
        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForInvalidFormat() throws Exception {

        // given:
        // when:
        Price.of("");
        // then:
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForInvalidFormat2() throws Exception {

        // given:
        // when:
        Price.of("abc");
        // then:
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForInvalidFormat3() throws Exception {

        // given:
        // when:
        Price.of("abc,de");
        // then:
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForInvalidFormat4() throws Exception {

        // given:
        // when:
        Price.of("123");
        // then:
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForInvalidFormat5() throws Exception {

        // given:
        // when:
        Price.of("123,455");
        // then:
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForInvalidFormat6() throws Exception {

        // given:
        // when:
        Price.of(",45");
        // then:
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForInvalidFormat7() throws Exception {
        
        // given:
        
        // when:
        Price.of("a 0,45");
        // then:
        
    }
    
    @Test
    public void shouldIgnoreLeadingAndTrailingWhitespaces() throws Exception {

        // given:
        
        // when:
        Price price = Price.of("  0,45  ");
        // then:
        assertThat(price).isEqualTo(Price.of("0,45"));
    }
    
    @Test
    public void shouldAddAnotherPrice() throws Exception {

        // given:
        
        // when:
        Price sum = Price.of("0,45").plus(Price.of("2,50"));
        
        // then:
        assertThat(sum).isEqualTo(Price.of("2,95"));
        
    }
    
    @Test
    public void shouldSubtractAnotherPrice() throws Exception {
        
        // given:
        
        // when:
        Price subtract = Price.of("0,99").minus(Price.of("0,50"));
        
        // then:
        assertThat(subtract).isEqualTo(Price.of("0,49"));
        
    }
    
}