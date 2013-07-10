package params;

import org.junit.Assert;
import org.junit.Test;

import beforeAfter.FizzBuzz;

public class FizzBuzzTest {

    private final FizzBuzz fizzBuzz = new FizzBuzz();
    
    @Test
    public void shouldAnswer1For1() throws Exception {

        Assert.assertEquals(fizzBuzz.answerFor(1), "1");
    }

    @Test
    public void shouldAnswer2For2() throws Exception {
        
        Assert.assertEquals(fizzBuzz.answerFor(2), "2");
    }

    @Test
    public void shouldAnswerFizzFor3() throws Exception {
        
        Assert.assertEquals(fizzBuzz.answerFor(3), "Fizz");
    }

    @Test
    public void shouldAnswerBuzzFor5() throws Exception {
        
        Assert.assertEquals(fizzBuzz.answerFor(5), "Buzz");
    }

    @Test
    public void shouldAnswerFizzFor6() throws Exception {
        
        Assert.assertEquals(fizzBuzz.answerFor(6), "Fizz");
    }
    
    @Test
    public void shouldAnswerBuzzFor10() throws Exception {
        
        Assert.assertEquals(fizzBuzz.answerFor(10), "Buzz");
    }
    
    @Test
    public void shouldAnswerFizzBuzzFor15() throws Exception {
        
        Assert.assertEquals(fizzBuzz.answerFor(15), "FizzBuzz");
    }

    @Test
    public void shouldAnswerFizzBuzzFor16() throws Exception {
        
        Assert.assertEquals(fizzBuzz.answerFor(16), "16");
    }
}
