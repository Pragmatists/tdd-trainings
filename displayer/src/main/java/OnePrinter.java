public class OnePrinter implements Printer {
	String[] chars = new String[] { "   ", "  |", "  |", "   " };

	public String printNumber(int level) {
		return chars[level];
	}
}
