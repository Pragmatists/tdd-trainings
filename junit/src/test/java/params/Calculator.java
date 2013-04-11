package params;


/**
 * Uruchom po kolei testy: 
 * CalculatorNoParamsTest, 
 * CalculatorHandMadeParamsTest, 
 * CalculatorJUnitParamsTest
 *
 * porownaj wyniki wykonania w oknie JUnit. 
 * Ktory sposob testowania z parametrami:
 * -- daje czytelne wyniki jesli test sie czerwieni lub niebieszczy?
 * -- daje czytelne wyniki jesli test sie zieleni?
 * -- daje mozliwosc przejscia bezposrednio do testowanej metody z okna JUnit?
 */
public class Calculator {

    public int sum(int ...factors) {
        int sum = 0;
        for (int factor : factors) {
            sum += factor;
        }
        return sum;
    }
    
    public long multiply(int ...factors) {
        int result = 1;
        for (int factor : factors) {
            result *= factor;
        }
        return result;
    }

}
