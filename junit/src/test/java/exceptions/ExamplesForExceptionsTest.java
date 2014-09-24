package exceptions;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.googlecode.catchexception.CatchException;

public class ExamplesForExceptionsTest {

	IntegerParser parser = new IntegerParser();

	// -----------------------------------------------
	// simple try-catch - not recommended
	@Test
	public void shouldFailMeaningfullyIfParsedStringIsNotANumber_simple()
			throws Exception {

		try {
			throwException();
			Assert.fail("expectedException!");

		} catch (Exception e) {
			Assert.assertTrue(e instanceof NumberFormatException);
		}
	}

	// -----------------------------------------------
	// junit expected exception - allows to check only class
	@Test(expected = NumberFormatException.class)
	public void shouldFailMeaningfullyIfParsedStringIsNotANumber_expectedOnTest()
			throws Exception {
		throwException();
	}

	// -----------------------------------------------
	// junit rule for expected exception - recommended way
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void shouldFailMeaningfullyIfParsedStringIsNotANumber_expectedRule() {
		// notice that we call methods on expectedException BEFORE
		// executing code that will throw exception
		expectedException.expect(NumberFormatException.class);
		expectedException.expectMessage("For input string: \"NaN\"");
		throwException();
	}

	// -----------------------------------------------
	// CatchException library - very good for advanced assertions
	// cannot be used for static calls and for constructors

	@Test
	public void shouldFailMeaningfullyIfParsedStringIsNotANumber_catchException() {
		CatchException.catchException(parser).parse("NaN");
		Assertions.assertThat(CatchException.caughtException()).isInstanceOf(
				NumberFormatException.class);
		Assertions.assertThat(CatchException.caughtException()).hasMessage(
				"For input string: \"NaN\"");
	}

	private void throwException() {
		parser.parse("NaN");
	}

}
