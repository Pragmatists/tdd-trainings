import static org.junit.Assert.*;

import org.junit.*;

public class DisplayerTest {
	@Test
	public void shouldReturnEmptyResult() throws Exception {
		Display display = new Display();

		String[] result = display.watch("");

		assertEquals("", result[0]);
		assertEquals("", result[1]);
		assertEquals("", result[2]);
		assertEquals("", result[3]);
	}

	@Test
	public void shouldReturnZero() throws Exception {
		Display display = new Display();

		String[] result = display.watch("0");

		assertEquals(" - ", result[0]);
		assertEquals("| |", result[1]);
		assertEquals("| |", result[2]);
		assertEquals(" - ", result[3]);
	}

	@Test
	public void shouldReturnOne() throws Exception {
		Display display = new Display();

		String[] result = display.watch("1");

		assertEquals("   ", result[0]);
		assertEquals("  |", result[1]);
		assertEquals("  |", result[2]);
		assertEquals("   ", result[3]);
	}

	@Test
	public void shouldReturn2Zero() throws Exception {
		Display display = new Display();

		String[] result = display.watch("00");

		assertEquals(" -  - ", result[0]);
		assertEquals("| || |", result[1]);
		assertEquals("| || |", result[2]);
		assertEquals(" -  - ", result[3]);
	}
}
