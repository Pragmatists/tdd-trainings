package tdd.dependencyBreaking.priceList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PriceList {

    private Map<String, Integer> prices = new HashMap<String, Integer>();
    
    public void loadFromFile(File fileWithPrices) throws FileNotFoundException{
        
        Scanner scanner = new Scanner(fileWithPrices);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String productName = line.split(":")[0];
            Integer price = Integer.valueOf(line.split(":")[1]);
            prices.put(productName, price);
        }
    }
    
    public Integer priceFor(String productName){
        
        if(!prices.containsKey(productName)){
            throw new IllegalArgumentException("No price for product: " + productName);
        }
        
        return prices.get(productName);
    }
}
