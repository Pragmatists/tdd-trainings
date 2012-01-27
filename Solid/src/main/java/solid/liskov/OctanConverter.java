package solid.liskov;

public class OctanConverter extends HexConverter {

	@Override
	public int convert(String text) {
		return convert(text,8);
	}
	
}
