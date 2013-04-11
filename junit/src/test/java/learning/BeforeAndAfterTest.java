package learning;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BeforeAndAfterTest {

    /**
     * Uruchom ten test i przyjrzyj sie outputowi na konsoli.
     * -- Jaka jest kolejnosc wywolan metod anotowanych @BeforeClass, @Before, @AfterClass, @After ?
     * -- Dlaczego metody @Before i @After sa wywolywane dwa razy, a @BeforeClass, @AfterClass po razie?
     * -- Dlaczego dwa razy wypisywane jest "test1: 42" mimo, ze counter jest postinkrementowany w obu 
     * metodach testowych?
     */
    
    private int counter = 42;
    
    @BeforeClass
    public static void setUpContext() {
        System.out.println("beforeClass");
    }
    
    @AfterClass
    public static void tearDownForTheWholeClass() {
        System.out.println("afterClass");
    }
    
    @Before
    public void commonSetupForEachTestMethod() {
        System.out.println("before");
    }
    
    @After
    public void commonTearDownForEachTestMethod() {
        System.out.println("after");
    }
    
    @Test
    public void test1() throws Exception {
        System.out.println("test1: " + counter++ );
    }
    
    @Test
    public void test2() throws Exception {
        System.out.println("test2: " + counter++ );
    }
}
