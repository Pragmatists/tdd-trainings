package tdd.dependencyBreaking.order2;

public final class OrderProcessor {

    public OrderProcessor() {
        throw new RuntimeException();
    }
    
    public void submit(Order order){
        
        // ... lot of complex logic
        
        int total = 0;
        for(OrderItem item: order.getItems()){
        
            if(!item.isBillable()){
                continue;
            }
            
            total += item.getPrice() * item.getQuantity();
        }
        
        System.out.println("Total: " + total);

        // ... lot of complex logic
    }

}
