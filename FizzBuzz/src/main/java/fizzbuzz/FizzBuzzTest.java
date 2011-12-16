package fizzbuzz;

import static org.junit.Assert.*;

import org.junit.*;

public class FizzBuzzTest {
	@Test
	public void inputNormalNumber() {
		FizzBuzz fizzBuzz = new FizzBuzz();
		assertEquals("17", fizzBuzz.play(17));
	}

	@Test
	public void input1() {
		FizzBuzz fizzBuzz = new FizzBuzz();
		assertEquals("1", fizzBuzz.play(1));
	}

	@Test
	public void input3() {
		FizzBuzz fizzBuzz = new FizzBuzz();
		assertEquals("Fizz", fizzBuzz.play(3));
	}

	@Test
	public void input5() {
		FizzBuzz fizzBuzz = new FizzBuzz();
		assertEquals("Buzz", fizzBuzz.play(5));
	}

	@Test
	public void input10() {
		FizzBuzz fizzBuzz = new FizzBuzz();
		assertEquals("Buzz", fizzBuzz.play(10));
	}

	@Test
	public void input15() {
		FizzBuzz fizzBuzz = new FizzBuzz();
		assertEquals("FizzBuzz", fizzBuzz.play(15));
	}

}
