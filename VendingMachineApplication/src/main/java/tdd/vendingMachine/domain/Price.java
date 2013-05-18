package tdd.vendingMachine.domain;

import java.util.regex.Pattern;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Price {

    private long value;
    private static Pattern pattern = Pattern.compile("\\d+,\\d{2}");

    public Price(long value) {
        this.value = value;
    }

    public static Price of(String price) {
        price = price.replaceAll("\\s", "");
        if (!pattern.matcher(price).matches()) {
            throw new IllegalArgumentException("Price has to be in following format: ###,##");
        }
        return new Price(Long.parseLong(price.replace(",", "")));
    }

    public long asCents() {
        return value;
    }

    public static Price fromCents(long cents) {
        return new Price(cents);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public String toString() {
        return String.format("%d,%02d", value / 100, value % 100);
    }

    public Price plus(Price another) {
        return new Price(this.value + another.value);
    }

    public Price minus(Price another) {
        return new Price(this.value - another.value);
    }

}