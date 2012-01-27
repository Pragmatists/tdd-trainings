package pl.mimuw.tdd.codeanalyzer;


public class CodeAnalyzer {

	private int lineCount;
	private int totalChars;
	private LineLengthHistogram lineLengthHistogram = new LineLengthHistogram();
	private int widestLineLength;
	private int widestLineNumber;

	public void measureLine(String line) {
		increaseLineCount();
		increaseTotalChars(line.length());
		addLineToHistogram(line.length());
		recordWidestLine(line.length());
	}

	private void increaseTotalChars(int lineLength) {
		totalChars += lineLength;
	}

	private void increaseLineCount() {
		lineCount++;
	}

	private void recordWidestLine(int lineLength) {
		if (lineLength > widestLineLength) {
			widestLineNumber = lineCount;
			widestLineLength = lineLength;
		}
	}

	private void addLineToHistogram(int lineLength) {
		lineLengthHistogram.addLineToHistogram(lineLength);
	}

	public int getLineCount() {
		return lineCount;
	}

	public int getMeanLineWidth() {
		return totalChars / lineCount;
	}

	public int lineCountForWidth(int width) {
		return lineLengthHistogram.lineCountForWidth(width);
	}

	public int getWidestLineNumber() {
		return widestLineNumber;
	}

	public int getWidestLineLength() {
		return widestLineLength;
	}

}
