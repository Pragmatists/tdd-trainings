package params;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CalculatorJUnitParamsTest {

    private Calculator calculator;


    @Before
    public void setUp() {
        calculator = new Calculator();
    }
    
    @Parameters({"1,2, 3", "-1,2, 1"})
    @Test
    public void shouldCalculateSum(int arg1, int arg2, int expectedValue) {
        assertThat(calculator.sum(arg1, arg2)).isEqualTo(expectedValue);
    }
    
    @Parameters(method="sumParameters")
    @Test
    public void shouldCalculateSum2(int arg1, int arg2, int expectedValue) {
        assertThat(calculator.sum(arg1, arg2)).isEqualTo(expectedValue); 
    }
    
 
    // --
    public Object[] sumParameters() {
        return $($(1,5, sumsTo(6)), $(-1,2, sumsTo(1)));
    }

    private int sumsTo(int value) {
        return value;
    }
    
    
}
