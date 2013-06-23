package tdd.directory;

public class Bookshelf {

    public static void main(String[] args) {
        
        InMemoryDirectory directory = new InMemoryDirectory();
        
        directory.addElement(new Element("TDD by Example"));
        directory.addElement(new Element("Design Patterns"));
        directory.addElement(new Element("Clean Code"));
        directory.addElement(new Element("Refactoring"));
        directory.addElement(new Element("Working Effectively with Legacy Code")); 
        directory.addElement(new Element("Refactoring to Patterns"));
        directory.addElement(new Element("Domain Driven Design"));
        directory.addElement(new Element("Growing Object Oriented Software"));
        directory.addElement(new Element("xUnit Test Patterns"));
        directory.addElement(new Element("Object Oriented Reenginering Patterns"));
    
        directory.generateIndex();
        
        System.out.println("My bookshelf has " 
                + directory.getElementCount() 
                + " books:\n" 
                + directory.getElement("index").getText());
        
    }
    
}
