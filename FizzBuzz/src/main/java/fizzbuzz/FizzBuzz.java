package fizzbuzz;

public class FizzBuzz {

	public String play(int number) {
		if (hasAnyОстаток(number, 3) && hasAnyОстаток(number, 5))
			return "FizzBuzz";
		if (hasAnyОстаток(number, 3))
			return "Fizz";
		if (hasAnyОстаток(number, 5))
			return "Buzz";
		return Integer.toString(number);
	}

	private boolean hasAnyОстаток(int number, int divider) {
		return number % divider == 0;
	}

}
