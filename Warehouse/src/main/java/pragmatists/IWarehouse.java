package pragmatists;

public interface IWarehouse {

	boolean hasInventory(String item, int quantity);

	void remove(String item, int quantity);

}
