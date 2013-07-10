package pl.pragmatists.tdd.codeanalyzer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CodeAnalyzerTest {

	private static final String SOME_LINE_CONTENT = "some line content";
	private CodeAnalyzer codeAnalyzer = new CodeAnalyzer();

	@Test
	public void shouldCountLines() {
		codeAnalyzer.measureLine(SOME_LINE_CONTENT);
		assertEquals(1, codeAnalyzer.getLineCount());

		codeAnalyzer.measureLine(SOME_LINE_CONTENT);
		codeAnalyzer.measureLine(SOME_LINE_CONTENT);
		assertEquals(3, codeAnalyzer.getLineCount());
	}

	@Test
	public void shouldCountMeanWidth() {
		codeAnalyzer.measureLine(aLineOf(30));
		codeAnalyzer.measureLine(aLineOf(10));

		assertEquals(20, codeAnalyzer.getMeanLineWidth());
	}

	@Test
	public void shouldRecordLineCountForWidth() {
		codeAnalyzer.measureLine(aLineOf(30));
		codeAnalyzer.measureLine(aLineOf(30));
		codeAnalyzer.measureLine(aLineOf(10));

		assertEquals(2, codeAnalyzer.lineCountForWidth(30));
		assertEquals(1, codeAnalyzer.lineCountForWidth(10));
		assertEquals(0, codeAnalyzer.lineCountForWidth(100));
	}

	@Test
	public void shouldRecordWidestLineNumberAndWidth() {
		codeAnalyzer.measureLine(aLineOf(10));
		codeAnalyzer.measureLine(aLineOf(30));

		assertEquals(2, codeAnalyzer.getWidestLineNumber());
		assertEquals(30, codeAnalyzer.getWidestLineLength());
	}

	private String aLineOf(int chars) {
		StringBuilder stringBuilder = new StringBuilder(chars);
		for (int i = 0; i < chars; i++) {
			stringBuilder.append('A');
		}
		return stringBuilder.toString();
	}
}
