package tdd;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class RomanNumeralsTest {

    @Test
    public void provideRoman1() throws Exception {

        assertThat(RomanNumeral.getFor(1)).isEqualTo("I");

    }
}
