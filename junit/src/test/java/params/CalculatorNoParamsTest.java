package params;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class CalculatorNoParamsTest {
    
    private Calculator calculator;


    @Before
    public void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    public void shouldCalculateSum1() {
        assertThat(calculator.sum(1, 2, 3)).isEqualTo(6);
    }
    
    @Test
    public void shouldCalculateSum2() {
        assertThat(calculator.sum(-1, 2)).isEqualTo(1);
    }

}
