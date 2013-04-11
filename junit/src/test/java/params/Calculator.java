package params;

public class Calculator {

    public int sum(int ...factors) {
        int sum = 0;
        for (int factor : factors) {
            sum += factor;
        }
        return sum;
    }

}
