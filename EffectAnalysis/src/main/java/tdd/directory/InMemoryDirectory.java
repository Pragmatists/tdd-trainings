package tdd.directory;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDirectory {

    private List<Element> elements = new ArrayList<Element>();
    
    public void addElement(Element newElement){
        elements.add(newElement);
    }
    
    public void generateIndex(){
        
        Element index = new Element("index");
        for (Element element : elements) {
            index.addText(element.getName() + "\n");
        }
        
        addElement(index);
    }
    
    public int getElementCount(){
        return elements.size();
    }
    
    public Element getElement(String name){
        for (Element element : elements) {
            if(element.getName().equals(name)){
                return element;
            }
        }
        return null;
    }
}
