package solid.liskov;


public class HexConverter {

	public int convert(String text) {
		return convert(text,16);
	}

	protected int convert(String text, int radix) {
		return Integer.parseInt(text, radix);
	}
}
