public class ZeroPrinter implements Printer {

	String[] chars = new String[] { " - ", "| |", "| |", " - " };

	@Override
	public String printNumber(int level) {
		return chars[level];
	}

}
