package calculator;

import static org.fest.assertions.Assertions.*;

import org.junit.*;

public class AddingTest {
	@Test
	public void adding() throws Exception {
		int[] operands = new int[] { 1, 1 };

		int operationResult = new AddingOperation().performOperation(operands);

		assertThat(operationResult).isEqualTo(2);
	}
}
