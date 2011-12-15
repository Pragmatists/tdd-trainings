package calculator;

import static junitparams.JUnitParamsRunner.*;
import static org.fest.assertions.Assertions.*;
import junitparams.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class CalculatorTest {

	@Test
	@Parameters(method = "operationExamples")
	public void evaluate(String operation, int result) throws Exception {
		assertThat(Calculator.eval(operation)).isEqualTo(result);
	}

	@SuppressWarnings("unused")
	private Object[] operationExamples() {
		return $(
		        $("2+2", 4),
		        $("2-2", 0),
		        $("1+2+3", 6));
	}

	@Test
	@Parameters(method = "operationTypeExamples")
	public void findOperationType(String expression, Class<?> operationClass) throws Exception {
		Calculator calculator = new Calculator();

		Object strategy = calculator.operationStrategy(expression);

		assertThat(strategy).isInstanceOf(operationClass);
	}

	@SuppressWarnings("unused")
	private Object[] operationTypeExamples() {
		return $(
		        $("2+2", AddingOperation.class),
		        $("2-2", SubstractionOperation.class));
	}
}
