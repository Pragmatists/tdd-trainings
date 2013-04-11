package suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * Ta suite odpalada jedna klase testowa, testujaca Calculator.
 * 1. Uzupelnij ja o odpalenie pozostalych.
 * 2. Uzyj anotacji @ClassnameFilters zamiast @SuiteClasses, zeby osiagnac ten sam efekt [byc moze bedziesz musial dodac lib/cpsuite-1.2.6.jar do Build Path]
 * 3. Jak wyeksludowac SlowTest1? [!]
 *
 */

@RunWith(Suite.class)
@SuiteClasses( FastTest1.class)
public class ExampleSuite {

}
