package pl.pragmatists;

import static org.junit.Assert.*;

import org.junit.Test;

public class StackTest {

	private static final String ELEMENT = "element";
	private Stack stack = new Stack(99);

	@Test
	public void newStackShouldBeEmpty() {
		assertEquals(0, stack.size());
	}

	@Test
	public void stackSizeAfterPushShouldBeOne() {
		stack.push(ELEMENT);

		assertStackSize(1);
	}

	@Test
	public void stackSizeAfterPushAndPopShouldBeZero() {
		stack.push(ELEMENT);
		stack.pop();

		assertStackSize(0);
	}

	@Test(expected = StackOverflowException.class)
	public void cannotPushAFullStack() {
		stack = new Stack(0);

		stack.push(ELEMENT);
	}

	@Test(expected = StackUnderflowException.class)
	public void cannotPopAnEmptyStack() {
		stack.pop();
	}

	@Test
	public void shouldPopPreviouslyPushedElement() {
		stack.push(ELEMENT);

		String popped = stack.pop();

		assertEquals(ELEMENT, popped);
	}

	@Test
	public void shouldPopPreviouslyPushedElementsInReverseOrder() {
		stack.push("first");
		stack.push("second");

		String firstPopped = stack.pop();
		String secondPopped = stack.pop();

		assertEquals("second", firstPopped);
		assertEquals("first", secondPopped);
	}

	private void assertStackSize(int expectedSize) {
		assertEquals(expectedSize, stack.size());
	}

}
