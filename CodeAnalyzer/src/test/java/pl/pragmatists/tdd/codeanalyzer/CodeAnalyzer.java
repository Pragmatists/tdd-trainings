package pl.pragmatists.tdd.codeanalyzer;

import java.util.HashMap;
import java.util.Map;

public class CodeAnalyzer {

	private int lineCount;
	private int totalChars;
	private Map<Integer, Integer> lineLengths = new HashMap<Integer, Integer>();
	private int widestLineLength;
	private int widestLineNumber;

	public void measureLine(String line) {
		lineCount++;
		totalChars += line.length();
		if (!lineLengths.containsKey(line.length())) {
			lineLengths.put(line.length(), 1);
		} else {
			int currentCount = lineLengths.get(line.length());
			lineLengths.put(line.length(), ++currentCount);
		}

		if (line.length() > widestLineLength) {
			widestLineNumber = lineCount;
			widestLineLength = line.length();
		}
	}

	public int getLineCount() {
		return lineCount;
	}

	public int getMeanLineWidth() {
		return totalChars / lineCount;
	}

	public int lineCountForWidth(int width) {
		if (!lineLengths.containsKey(width)) {
			return 0;
		}
		return lineLengths.get(width);
	}

	public int getWidestLineNumber() {
		return widestLineNumber;
	}

	public int getWidestLineLength() {
		return widestLineLength;
	}

}
