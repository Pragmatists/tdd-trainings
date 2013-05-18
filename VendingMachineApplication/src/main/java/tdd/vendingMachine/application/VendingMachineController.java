package tdd.vendingMachine.application;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductStorage;
import tdd.vendingMachine.domain.VendingMachine;

@Controller
public class VendingMachineController {

    private static final int MAX_SHELF_NUMBER = 9;
    
    private final VendingMachine vendingMachine;
    private final ProductStorage productStorage;
    
    @RequestMapping("/")
    public String index(){
        return "redirect:vending-machine";
    }
    
    @RequestMapping("/vending-machine")
    public String page(){
        return "vending-machine";
    }
    
    @Autowired
    public VendingMachineController(VendingMachine vendingMachine, ProductStorage productStorage) {
        this.vendingMachine = vendingMachine;
        this.productStorage = productStorage;
    }

    @RequestMapping(   value = "/vending-machine/push",
                      method = RequestMethod.POST,
                    consumes = "application/json",
                    produces = "application/json")
    public @ResponseBody Response selectProduct(@RequestBody Integer shelfNumber) {
        
        try{
            vendingMachine.select(shelfNumber);
            return Response.success();

        } catch (Exception e) {
            return Response.failure(e.getMessage());
        }
    }

    @RequestMapping(   value = "/vending-machine/insert",
                      method = RequestMethod.POST,
                    consumes = "application/json",
                    produces = "application/json")
    public @ResponseBody Response insertCoin(@RequestBody String coin) {

        try{
            vendingMachine.insert(Coin.valueOf(coin));
            return Response.success();

        } catch (IllegalArgumentException e) {
            return Response.failure(String.format("Invalid argument: '%s'", coin));
        } catch (Exception e) {
            return Response.failure(e.getMessage());
        }
    }

    @RequestMapping(   value = "/vending-machine/state",
                      method = RequestMethod.GET,
                    produces = "application/json")
    public @ResponseBody VendingMachineDTO state(){
        return new VendingMachineDTO(vendingMachine.getDisplay(), buildDTO(productStorage));
    }
    
    private StorageDTO buildDTO(ProductStorage productStorage) {
        
        StorageDTO storage = new StorageDTO();
        
        for(int shelfNumber = 1; shelfNumber <= MAX_SHELF_NUMBER; shelfNumber++){
            Product product = productStorage.productOnShelf(shelfNumber);
            if(product != Product.NO_PRODUCT)
                storage.addShelf(shelfNumber, product, productStorage.itemsOnShelf(shelfNumber));
        }
        
        return storage;
    }

    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    public static class VendingMachineDTO {
        
        String display;
        StorageDTO storage;
        
        VendingMachineDTO(String display, StorageDTO storage) {
            this.storage = storage;
            this.display = display;
        }
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    public static class StorageDTO {

        Map<Integer, ShelfDTO> shelfs = new HashMap<Integer, ShelfDTO>();
     
        void addShelf(Integer shelfNumber, Product product, Integer items){
            shelfs.put(shelfNumber, new ShelfDTO(product, items));
        }
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    public static class ShelfDTO {
        
        Product product;
        Integer items;
        
        ShelfDTO(Product product, Integer items) {
            this.product = product;
            this.items = items;
        }
    }
    
}

