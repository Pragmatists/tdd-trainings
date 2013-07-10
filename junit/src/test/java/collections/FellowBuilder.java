package collections;

public class FellowBuilder {
    private String name;
    private String race;

    public FellowBuilder named(String name) {
        this.name = name;
        return this;
    }

    public Fellow race(String race) {
        this.race = race;
        return build();
    }

    private Fellow build() {
        return new Fellow(name, race);
    }
}
