package pl.pragmatists;

import java.util.ArrayList;
import java.util.List;

public class PrimeFactors {

	private static final int SMALLEST_PRIME = 2;
	private ArrayList<Integer> primes;
	private int candidate;
	private int number;

	public List<Integer> generate(int number) {
		this.number = number;
		primes = new ArrayList<Integer>();
		candidate = SMALLEST_PRIME;
		addPrimes();
		return primes;
	}

	private void addPrimes() {
		while (hasPrimes()) {
			while (isNumberDivisibleByCandidate()) {
				addCandidatePrime();
			}
			nextCandidate();
		}
	}

	private void nextCandidate() {
		candidate++;
	}

	private boolean hasPrimes() {
		return this.number > 1;
	}

	private void addCandidatePrime() {
		primes.add(candidate);
		this.number /= candidate;
	}

	private boolean isNumberDivisibleByCandidate() {
		return this.number % candidate == 0;
	}

}
