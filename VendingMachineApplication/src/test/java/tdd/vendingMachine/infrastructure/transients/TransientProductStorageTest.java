package tdd.vendingMachine.infrastructure.transients;

import org.junit.Before;

import tdd.vendingMachine.domain.ProductStorageContractTest;

public class TransientProductStorageTest extends ProductStorageContractTest{

    @Before
    public void setUp() {

        storage = new TransientProductStorage();
    }
    
}
