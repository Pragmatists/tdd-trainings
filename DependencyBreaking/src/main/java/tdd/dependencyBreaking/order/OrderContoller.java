package tdd.dependencyBreaking.order;

import java.util.List;

public class OrderContoller {

    private PriceList priceList;
    private TaxCalculator tax;

    public int total(List<Product> products, ShippingMethod method) {

        int total = 0;

        for (Product product : products) {
            int price = priceList.priceFor(product);
            total += price;
            total += tax.calculateTax(price);
        }

        int price = method.shippingPrice();
        total += price;
        total += tax.calculateTax(price);

        return total;
    }

}
