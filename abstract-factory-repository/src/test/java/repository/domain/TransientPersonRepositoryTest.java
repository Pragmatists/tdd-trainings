package repository.domain;

import org.junit.Before;

import repository.infrastructure.transients.PersonRepositoryContractTest;
import repository.infrastructure.transients.TransientPersonRepository;

public class TransientPersonRepositoryTest extends PersonRepositoryContractTest {

    @Before
    public void setUp() {

        repository = new TransientPersonRepository();
    }

}
