package calculator;

public class Calculator {

	public static int eval(String operation) {
		Calculator calculator = new Calculator();
		return calculator.evalOperation(operation);
	}

	private int evalOperation(String expression) {
		Operation operation = operationStrategy(expression);
		int[] operands = operation.parseOperation(expression);
		return operation.performOperation(operands);
	}

	public Operation operationStrategy(String expression) {
		if (expression.indexOf("+") > 0) {
			return new AddingOperation();
		} else if (expression.indexOf("-") > 0) {
			return new SubstractionOperation();
		}
		return null;
	}
}
