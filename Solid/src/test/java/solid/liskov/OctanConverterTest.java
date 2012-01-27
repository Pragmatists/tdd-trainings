package solid.liskov;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class OctanConverterTest {

	@Test
	public void shouldConvertFromOctan() {
		assertConverted(0, "0");
		assertConverted(7, "7");
		assertConverted(15, "17");
	}
	
	private void assertConverted(int decimal, String hex) {
		assertEquals(decimal, createConverter().convert(hex));
	}

	protected HexConverter createConverter() {
		return new OctanConverter();
	}
	
}
