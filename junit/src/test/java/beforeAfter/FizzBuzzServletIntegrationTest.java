package beforeAfter;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * 
 	TODO: Przyspiesz testy. Podpowiedź: użyj @BeforeClass i @AfterClass.
 */
@RunWith(Parameterized.class)
public class FizzBuzzServletIntegrationTest {

    private EmbeddedServer server;

    private final String number;
    private final String expectedAnswer;
    
    public FizzBuzzServletIntegrationTest(String number, String answer) {
        this.number = number;
        this.expectedAnswer = answer;
    }

    @Before
    public void setUp(){
        server = new EmbeddedServer(9987);
        server.start();
    }
    
    @After
    public void tearDown(){
        server.stop();
    }
    
    @Test
    public void shouldGetAnswerFromServer() throws Exception {

        // given:
        
        // when:
        String answer = get("?number=" + number);
        
        // then:
        Assert.assertEquals(expectedAnswer, answer);
    }
    
    @Parameters
    public static List<Object[]> parameters(){
        return Arrays.<Object[]>asList(
                testCase("1", "1"),
                testCase("2", "2"),
                testCase("3", "Fizz"),
                testCase("4", "4"),
                testCase("5", "Buzz"),
                testCase("6", "Fizz"),
                testCase("7", "7"),
                testCase("8", "8"),
                testCase("9", "Fizz"),
                testCase("10", "Buzz"),
                testCase("11", "11"),
                testCase("12", "Fizz"),
                testCase("13", "13"),
                testCase("14", "14"),
                testCase("15", "FizzBuzz"),
                testCase("20", "Buzz"),
                testCase("53", "53"),
                testCase("55", "Buzz"),
                testCase("122475", "FizzBuzz"),
                testCase("233552234", "233552234")
                );
    }

    // --
    
    private static Object[] testCase(String number, String answer) {
        return new Object[]{number, answer};
    }
    
    private String get(String params) throws Exception {
        
        URL url = new URL("http://localhost:9987/fizzBuzz" + params);
        
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        IOUtils.copy(url.openStream(), response);
        
        return response.toString();
    }
}
