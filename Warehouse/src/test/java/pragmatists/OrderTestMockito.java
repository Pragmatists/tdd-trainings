package pragmatists;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderTestMockito {
	@Mock
	private Warehouse warehouse;

	private static final String ZIEMNIAKI = "ziemniaki";

	@Test
	public void orderShouldBeFilled() {
		doReturn(true).when(warehouse).hasInventory(ZIEMNIAKI, 50);
		Order order = new Order(ZIEMNIAKI, 50);

		order.fill(warehouse);

		assertTrue(order.isFilled());
		verify(warehouse).remove(ZIEMNIAKI, 50);

	}

	@Test
	public void orderShouldNotBeFilled() {
		when(warehouse.hasInventory(ZIEMNIAKI, 51)).thenReturn(false);
		Order order = new Order(ZIEMNIAKI, 51);

		order.fill(warehouse);

		assertFalse(order.isFilled());
		verify(warehouse, never()).remove(ZIEMNIAKI, 50);
	}
}
