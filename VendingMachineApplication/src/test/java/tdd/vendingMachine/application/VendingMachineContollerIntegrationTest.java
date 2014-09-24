package tdd.vendingMachine.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductStorage;
import tdd.vendingMachine.domain.VendingMachine;

public class VendingMachineContollerIntegrationTest extends IntegrationTest{

    @Autowired
    private VendingMachine vendingMachine;
    
    @Autowired
    private ProductStorage storage;
    
    public VendingMachineContollerIntegrationTest() {
        super("http://localhost:9900/vending-machine/");
    }

    @Before
    public void clearStorage() {
        vendingMachine.cancel();
        storage.clear();
    }
    
    @Test
    public void shouldReturnVendingMachineState_ShelfsContent() throws Exception {

        // given:
        storage.loadOnShelf(1, new Product("Mineral Water"));
        storage.loadOnShelf(2, new Product("Chocolate Bar"));
        storage.loadOnShelf(2, new Product("Chocolate Bar"));
        
        // when:
        String response = get("state");
        
        // then:
        assertThat(response).contains(
                json("'storage':{" + 
                        "'shelfs':{" +
                            "'1':{'product':{'name':'Mineral Water'},'items':1}," +
                            "'2':{'product':{'name':'Chocolate Bar'},'items':2}" +
                        "}" +
                     "}"
                    ));
    }

    @Test
    public void shouldReturnVendingMachineState_Display() throws Exception {
        
        // given:
        storage.loadOnShelf(1, new Product("Mineral Water"));
        
        // when:
        post("push", json("1"));
        String response = get("state");
        
        // then:
        assertThat(response).contains(
                json("'display':'Mineral Water: 2,50 PLN'"));
    }
    
    @Test
    public void shouldReturnVendingMachineState_DisplayAfterInsertingCoin() throws Exception {
        
        // given:
        storage.loadOnShelf(1, new Product("Mineral Water"));
        
        // when:
        post("push", json("1"));
        post("insert", json("'ONE_DOLLAR'"));
        String response = get("state");
        
        // then:
        assertThat(response).contains(
                json("'display':'Mineral Water: 1,50 PLN'"));
    }
    
    
}
