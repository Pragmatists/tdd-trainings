package pl.mimuw.tdd.codeanalyzer;

import java.util.HashMap;
import java.util.Map;

public class LineLengthHistogram {
	public Map<Integer, Integer> lineLengths = new HashMap<Integer, Integer>();

	public void addLineToHistogram(int lineLength) {
		if (noEntryFor(lineLength)) {
			addFirstOccurence(lineLength);
		} else {
			updateHistogram(lineLength);
		}
	}

	public int lineCountForWidth(int width) {
		if (noEntryFor(width)) {
			return 0;
		}
		return lineLengths.get(width);
	}

	private boolean noEntryFor(int lineLength) {
		return !lineLengths.containsKey(lineLength);
	}

	private Integer addFirstOccurence(int lineLength) {
		return lineLengths.put(lineLength, 1);
	}

	private void updateHistogram(int lineLength) {
		int currentCount = lineLengths.get(lineLength);
		lineLengths.put(lineLength, ++currentCount);
	}

}