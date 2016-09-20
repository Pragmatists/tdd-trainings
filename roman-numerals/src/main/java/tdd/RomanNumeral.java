package tdd;

import java.util.ArrayList;
import java.util.List;

public class RomanNumeral {

    private List<Chunk> chunks = new ArrayList<>();

    public RomanNumeral() {
        chunks.add(new Chunk(1000, "M"));
        chunks.add(new Chunk(900, "CM"));
        chunks.add(new Chunk(500, "D"));
        chunks.add(new Chunk(400, "CD"));
        chunks.add(new Chunk(100, "C"));
        chunks.add(new Chunk(90, "XC"));
        chunks.add(new Chunk(50, "L"));
        chunks.add(new Chunk(40, "XL"));
        chunks.add(new Chunk(10, "X"));
        chunks.add(new Chunk(9, "IX"));
        chunks.add(new Chunk(5, "V"));
        chunks.add(new Chunk(4, "IV"));
        chunks.add(new Chunk(1, "I"));
    }

    public String getFor(int number) {
        if (number <= 0) {
            return "";
        }
        Chunk highest = chunks.stream()
                .filter(chunk -> chunk.arabic <= number)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        return highest.roman + getFor(number - highest.arabic);
    }

    private class Chunk {
        private final int arabic;

        private final String roman;

        Chunk(int arabic, String roman) {
            this.arabic = arabic;
            this.roman = roman;
        }
    }
}
