package params;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class CalculatorHandMadeParamsTest {

    
    private Calculator calculator;

    @Before
    public void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    public void shouldCalculateSum1() {
        assertThatAccordingTo(calculator).sumOf(1, 2, 3).is(6); 
    }
    
    @Test
    public void shouldCalculateSum2() {
        assertThatAccordingTo(calculator).sumOf(-1, 2).is(1); 
    }
    
    // --
    
    private CalculatorAssert assertThatAccordingTo(Calculator calculator) {
        return new CalculatorAssert(calculator);
    }
    
    public class CalculatorAssert {

        private Calculator calculator;
        private int[] args;

        public CalculatorAssert(Calculator calculator) {
            this.calculator = calculator;
        }

        public CalculatorAssert sumOf(int ...args) {
            this.args = args;
            return this;
        }

        public void is(int expectedValue) {
            assertThat(calculator.sum(args)).as("Sum of " + Arrays.toString(args)).isEqualTo(expectedValue);
        }

    }
}
