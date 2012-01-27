package solid.liskov;

import static org.junit.Assert.*;

import org.junit.Test;

public class HexConverterTest {

	@Test
	public void shouldConvertFromHex() {
		assertConverted(0, "0");
		assertConverted(15, "F");
		assertConverted(16, "10");
	}
	
	private void assertConverted(int decimal, String hex) {
		assertEquals(decimal, createConverter().convert(hex));
	}

	protected HexConverter createConverter() {
		return new HexConverter();
	}
	
}
