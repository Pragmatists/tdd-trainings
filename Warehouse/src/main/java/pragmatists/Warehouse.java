package pragmatists;

import java.util.HashMap;
import java.util.Map;

public class Warehouse implements IWarehouse {
	Map<String, Integer> inventory = new HashMap<String, Integer>();

	public boolean hasInventory(String item, int quantity) {
		return inventory.get(item) >= quantity;
	}

	public void remove(String item, int quantity) {
		inventory.put(item, inventory.get(item) - quantity);
	}

	public void add(String item, int quantity) {
		inventory.put(item, quantity);
	}

	public Object getInventory(String item) {
		return inventory.get(item);
	}

	public void assign(Person person) {
		// TODO Auto-generated method stub

	}

}
