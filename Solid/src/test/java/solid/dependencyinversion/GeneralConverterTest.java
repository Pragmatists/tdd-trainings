package solid.dependencyinversion;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class GeneralConverterTest {

	@Test
	public void shouldConvertFromHex() {
		int value = new GeneralConverter().convert("F", NumberSystem.HEX);

		assertEquals(15, value);
	}

	@Test
	public void shouldConvertFromOctan() {
		int value = new GeneralConverter().convert("17", NumberSystem.OCTAN);

		assertEquals(15, value);
	}

}
