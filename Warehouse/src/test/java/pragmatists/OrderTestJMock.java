package pragmatists;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class OrderTestJMock {

	private static final String ZIEMNIAKI = "ziemniaki";
	private Mockery context = new JUnit4Mockery();
	private IWarehouse warehouse = context.mock(IWarehouse.class);

	@Test
	public void orderShouldBeFilled() {
		context.checking(new Expectations() {
			{
				oneOf(warehouse).hasInventory(ZIEMNIAKI, 50);
				will(returnValue(true));
				oneOf(warehouse).remove(ZIEMNIAKI, 50);
			}
		});
		Order order = new Order(ZIEMNIAKI, 50);

		order.fill(warehouse);

		assertTrue(order.isFilled());
	}

	@Test
	public void orderShouldNotBeFilled() {
		context.checking(new Expectations() {
			{
				oneOf(warehouse).hasInventory(ZIEMNIAKI, 51);
				will(returnValue(false));
			}
		});
		Order order = new Order(ZIEMNIAKI, 51);

		order.fill(warehouse);

		assertFalse(order.isFilled());
	}
}
