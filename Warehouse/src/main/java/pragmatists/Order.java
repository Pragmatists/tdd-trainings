package pragmatists;

public class Order {

	private final String item;
	private final int quantity;
	private boolean filled = false;

	public Order(String item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public void fill(IWarehouse warehouse) {
		if (warehouse.hasInventory(item, quantity)) {
			warehouse.remove(item, quantity);
			filled = true;
		}
	}

	public boolean isFilled() {
		return filled;
	}

}
