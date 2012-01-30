package pl.pragmatists;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PrimeFactorsTest {

	@Test
	public void one() {
		assertPrimes(list(), 1);
	}

	@Test
	public void two() {
		assertPrimes(list(2), 2);
	}

	@Test
	public void three() {
		assertPrimes(list(3), 3);
	}

	@Test
	public void four() {
		assertPrimes(list(2, 2), 4);
	}

	@Test
	public void six() {
		assertPrimes(list(2, 3), 6);
	}

	@Test
	public void eight() {
		assertPrimes(list(2, 2, 2), 8);
	}

	@Test
	public void nine() {
		assertPrimes(list(3, 3), 9);
	}

	private void assertPrimes(ArrayList<Integer> expectedPrimes, int number) {
		List<Integer> primes = new PrimeFactors().generate(number);

		assertEquals(expectedPrimes, primes);
	}

	private ArrayList<Integer> list(int... numbers) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int number : numbers) {
			result.add(number);
		}
		return result;
	}
}
