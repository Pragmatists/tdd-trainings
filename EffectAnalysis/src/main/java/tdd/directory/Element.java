package tdd.directory;

public class Element {

    private final String name;
    private String text = "";

    public Element(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addText(String text) {
        this.text += text;
    }

    public String getText() {
        return text;
    }
}
