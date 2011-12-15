package calculator;

public abstract class Operation {

	protected abstract int[] parseOperation(String operation);

	int performOperation(int[] operands) {
		return performOperation(operands[0], operands[1]);
	}

	protected abstract int performOperation(int operand1, int operand2);

	protected int[] getOperands(String operation, String pattern) {
		String[] operands = operation.split(pattern);

		int[] result = new int[operands.length];
		for (int i = 0; i < operands.length; i++)
			result[i] = Integer.parseInt(operands[i]);

		return result;
	}

}