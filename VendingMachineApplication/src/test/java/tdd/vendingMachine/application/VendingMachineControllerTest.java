package tdd.vendingMachine.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import tdd.vendingMachine.application.VendingMachineController.StorageDTO;
import tdd.vendingMachine.application.VendingMachineController.VendingMachineDTO;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductStorage;
import tdd.vendingMachine.domain.VendingMachine;
import tdd.vendingMachine.infrastructure.transients.TransientProductStorage;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineControllerTest {

    @Mock
    private VendingMachine vendingMachine;

    private VendingMachineController controller;

    @Before
    public void setUp() {

        controller = new VendingMachineController(vendingMachine, null);
    }

    @Test
    public void shouldSelectProduct() throws Exception {

        // given:
        // when:
        controller.selectProduct(3);

        // then:
        verify(vendingMachine).select(3);
    }

    @Test
    public void shouldReturnSuccessForSelect() throws Exception {

        // given:
        // when:
        Response response = controller.selectProduct(3);

        // then:
        assertThat(response).isEqualTo(Response.success());
    }

    @Test
    public void shouldReturnFailureForSelect() throws Exception {

        // given:
        selectFailsWithMessage("Invalid shelf number");

        // when:
        Response response = controller.selectProduct(-1);

        // then:
        assertThat(response).isEqualTo(Response.failure("Invalid shelf number"));
    }

    @Test
    public void shouldInsertCoin() throws Exception {

        // given:
        // when:
        controller.insertCoin("ONE_DOLLAR");

        // then:
        verify(vendingMachine).insert(Coin.ONE_DOLLAR);
    }

    @Test
    public void shouldReturnSuccessForInsertCoin() throws Exception {

        // given:
        // when:
        Response response = controller.insertCoin("ONE_DOLLAR");

        // then:
        assertThat(response).isEqualTo(Response.success());
    }

    @Test
    public void shouldReturnFailureForInsertCoin() throws Exception {

        // given:
        // when:
        Response response = controller.insertCoin("INVALID_COIN");

        // then:
        assertThat(response).isEqualTo(Response.failure("Invalid argument: 'INVALID_COIN'"));
    }
    
    @Test
    public void shouldReturnStateOfStorage() throws Exception {

        // given:
        ProductStorage storage = new TransientProductStorage();
        storage.loadOnShelf(1, new Product("Product A"));
        storage.loadOnShelf(2, new Product("Product B"));
        storage.loadOnShelf(2, new Product("Product B"));
        
        controller = new VendingMachineController(vendingMachine, storage);
        
        // when:
        VendingMachineDTO state = controller.state();
        
        // then:
        StorageDTO storageDTO = new StorageDTO();
        storageDTO.addShelf(1, new Product("Product A"), 1);
        storageDTO.addShelf(2, new Product("Product B"), 2);
        assertThat(state).isEqualsToByComparingFields(new VendingMachineDTO("", storageDTO));
    }
    
    // --

    private void selectFailsWithMessage(String message) {
        doThrow(new IllegalArgumentException(message)).when(vendingMachine).select(Mockito.anyInt());
    }

}
