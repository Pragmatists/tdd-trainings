import spock.lang.Specification

class MaxSpec extends Specification {
    def "vv "() {

    }

    def "maximum of two numbers"(int a, int b, int expectedMax) {
        expect:
        Math.max(a, b) == expectedMax

        where:
        a | b | expectedMax
        1 | 3 | 3
        7 | 4 | 7
        0 | 0 | 0
    }
}