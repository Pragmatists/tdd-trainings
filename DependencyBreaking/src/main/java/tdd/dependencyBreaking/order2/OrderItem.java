package tdd.dependencyBreaking.order2;

public class OrderItem {

    private int price;
    private int quantity;
    
    private OrderItem(int price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public boolean isBillable() {
        return true;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

}
