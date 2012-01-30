package pl.pragmatists;

import static org.junit.Assert.*;

import org.junit.Test;

public class StackTest {
	private static final String ELEMENT = "element";
	private Stack stack = new Stack(100);

	@Test
	public void newStackShouldBeEmpty() {

		assertSize(0);
	}

	@Test
	public void stackSizeAfterPushShouldBeOne() {
		stack.push(ELEMENT);

		assertSize(1);
	}

	@Test
	public void stackSizeAfterPushingTwiceShouldBeTwo() {
		stack.push(ELEMENT);
		stack.push(ELEMENT);

		assertSize(2);
	}

	@Test
	public void stackSizeAfterPushAndPopShouldBeZero() {
		stack.push(ELEMENT);
		stack.pop();

		assertSize(0);
	}

	@Test(expected = StackUnderflowException.class)
	public void cannotPopAnEmptyStack() {
		stack.pop();
	}

	@Test(expected = StackOverflowException.class)
	public void cannotPushAnFullStack() {
		stack = new Stack(0);

		stack.push(ELEMENT);
	}

	@Test
	public void shouldPopPreviouslyPushedElement() {
		stack.push(ELEMENT);

		String popped = stack.pop();

		assertEquals(ELEMENT, popped);
	}

	@Test
	public void shouldPopPushedElementsInReverseOrder() {
		stack.push("A");
		stack.push("B");

		String first = stack.pop();
		String second = stack.pop();

		assertEquals("B", first);
		assertEquals("A", second);
	}

	protected void assertSize(int expectedSize) {
		assertEquals(expectedSize, stack.size());
	}
}
