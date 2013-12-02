package pragmatists;

import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class OrderTestEasyMock {

	private static final String ZIEMNIAKI = "ziemniaki";
	private IWarehouse warehouse = createMock(IWarehouse.class);

	@Test
	public void orderShouldBeFilled() {
		expect(warehouse.hasInventory(ZIEMNIAKI, 50)).andReturn(true);
		warehouse.remove(ZIEMNIAKI, 50);
		replay(warehouse);
		Order order = new Order(ZIEMNIAKI, 50);

		order.fill(warehouse);

		assertTrue(order.isFilled());
		verify(warehouse);
	}

	@Test
	public void orderShouldNotBeFilled() {
		Order order = new Order(ZIEMNIAKI, 51);

		order.fill(warehouse);

		assertFalse(order.isFilled());
	}
}
