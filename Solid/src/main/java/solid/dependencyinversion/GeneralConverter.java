package solid.dependencyinversion;

import solid.liskov.HexConverter;
import solid.liskov.OctanConverter;

public class GeneralConverter {

	public int convert(String text, NumberSystem numberSystem) {
		if (numberSystem == NumberSystem.HEX)
			return new HexConverter().convert(text);
		else
			return new OctanConverter().convert(text);
	}

}
