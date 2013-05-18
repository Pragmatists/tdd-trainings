package tdd.junit;

import org.junit.Assert;
import org.junit.Test;

public class IntegerParsingTest {

    @Test
    public void shouldFailMeaningfullyIfParsedStringIsNotANumber() throws Exception {

        try{
            
            // given:
            // when:
            Integer.parseInt("NaN");
            Assert.fail("expectedException!");
            
        } catch (Exception e){
            // then:
            Assert.assertTrue(e instanceof NumberFormatException);
        }
    }
    
    @Test
    public void shouldFailMeaningfullyIfParsedStringRepresentToLargeNumber() throws Exception {
        
        try{
            
            // given:
            Long tooLarge = (long)Integer.MAX_VALUE + 1L;
            
            // when:
            Integer.parseInt(tooLarge.toString());
            Assert.fail("expectedException!");
            
        } catch (Exception e){

            // then:
            Assert.assertTrue(e instanceof NumberFormatException);
        }
    }
    
}
