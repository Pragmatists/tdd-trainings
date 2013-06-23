package tdd.dependencyBreaking.order2;

import java.util.List;

public class Order {

    private List<OrderItem> items;

    private Order(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderItem> getItems() {
        return items;
    }

}
