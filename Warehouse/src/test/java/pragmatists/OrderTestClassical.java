package pragmatists;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OrderTestClassical {

	private static final String ZIEMNIAKI = "ziemniaki";
	private Warehouse warehouse = new Warehouse();

	@Before
	public void setUpWerhouse() {
		warehouse.add(ZIEMNIAKI, 50);
	}

	@Test
	public void orderShouldBeFilled() {
		Order order = new Order(ZIEMNIAKI, 50);

		order.fill(warehouse);

		assertTrue(order.isFilled());
		assertEquals(0, warehouse.getInventory(ZIEMNIAKI));
	}

	@Test
	public void orderShouldNotBeFilled() {
		Order order = new Order(ZIEMNIAKI, 51);

		order.fill(warehouse);

		assertFalse(order.isFilled());
		assertEquals(50, warehouse.getInventory(ZIEMNIAKI));
	}
}
