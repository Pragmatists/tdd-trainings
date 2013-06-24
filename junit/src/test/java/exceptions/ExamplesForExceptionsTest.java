package exceptions;

import org.fest.assertions.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.googlecode.catchexception.CatchException;

public class ExamplesForExceptionsTest {

	IntegerParser parser = new IntegerParser();
	
	@Test
	public void shouldFailMeaningfullyIfParsedStringIsNotANumber_simple()
			throws Exception {

		try {
			parser.parse("NaN");
			Assert.fail("expectedException!");

		} catch (Exception e) {
			Assert.assertTrue(e instanceof NumberFormatException);
		}
	}

	@Test(expected = NumberFormatException.class)
	public void shouldFailMeaningfullyIfParsedStringIsNotANumber_expectedOnTest()
			throws Exception {
		Integer.parseInt("NaN");
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void shouldFailMeaningfullyIfParsedStringIsNotANumber_expectedRule()
			throws Exception {
		expectedException.expect(NumberFormatException.class);
		expectedException.expectMessage("For input string: \"NaN\"");
		parser.parse("NaN");
	}

	@Test
	public void shouldFailMeaningfullyIfParsedStringIsNotANumber_catchException()
			throws Exception {
		CatchException.catchException(parser).parse("NaN");
		Assertions.assertThat(CatchException.caughtException()).isInstanceOf(NumberFormatException.class);
		Assertions.assertThat(CatchException.caughtException()).hasMessage("For input string: \"NaN\"");
	}
	

}
