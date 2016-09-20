package tdd;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class RomanNumeralTest {

    @Test
    @Parameters({
            "1,I",
            "4,IV",
            "5,V",
            "10,X",
            "40,XL",
            "50,L",
            "100,C",
            "400,CD",
            "500,D",
            "900,CM",
            "1000,M",
            "2000,MM",
            "1944,MCMXLIV",
    })
    public void provideRoman(int arabic, String roman) throws Exception {
        assertThat(romanRepresentationOf(arabic)).isEqualTo(roman);
    }

    private String romanRepresentationOf(int arabic) {
        return new RomanNumeral().getFor(arabic);
    }
}
