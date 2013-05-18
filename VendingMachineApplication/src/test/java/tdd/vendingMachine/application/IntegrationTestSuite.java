package tdd.vendingMachine.application;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.context.ApplicationContext;

import tdd.vendingMachine.infrastructure.jdbc.JdbcProductStorageIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({VendingMachineContollerIntegrationTest.class, JdbcProductStorageIntegrationTest.class})
public class IntegrationTestSuite {

    private static EmbeddedServer server;

    @BeforeClass
    public static void setUp() {
        server = new EmbeddedServer(9900);
        server.start();
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }
 
    public static ApplicationContext context() {
        return Context.get(server.server);
    }
}
