import java.util.*;

public class Display {

	Map<Character, Printer> printers = new HashMap<Character, Printer>();

	public String[] watch(String numbers) {
		printers.put('0', new ZeroPrinter());
		printers.put('1', new OnePrinter());

		String[] result = new String[4];
		for (int level = 0; level < 4; level++)
			result[level] = delegateLevelBuilding(numbers, level);

		return result;
	}

	private String delegateLevelBuilding(String numbers, int level) {
		String result = "";
		for (Character character : numbers.toCharArray()) {
			result += printers.get(character).printNumber(level);
		}

		return result;
	}
}
