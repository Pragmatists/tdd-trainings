package calculator;

public class SubstractionOperation extends Operation {

	@Override
	protected int[] parseOperation(String operation) {
		return getOperands(operation, "\\-");
	}

	@Override
	protected int performOperation(int operand1, int operand2) {
		return operand1 - operand2;
	}

}
