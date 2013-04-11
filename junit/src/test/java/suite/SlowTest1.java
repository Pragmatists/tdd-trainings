package suite;

import org.junit.Test;

public class SlowTest1 {

    @Test
    public void test() throws Exception {
        Thread.sleep(60000);
    }
    
}
