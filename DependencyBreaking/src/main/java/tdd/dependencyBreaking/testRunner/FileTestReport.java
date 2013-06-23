package tdd.dependencyBreaking.testRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileTestReport {

    private FileWriter writer;
    
    public FileTestReport(File file) throws IOException{
        
        writer = new FileWriter(file);
    }
    
    public void reportSuccess(TestCase test) {
        write("Succesful: " + test.getClass() + "\n");
    }

    public void reportFailure(TestCase test) {
        write("Failure: " + test.getClass() + "\n");
    }
    
    private void write(String line){
        
        try{
            writer.append(line);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public void close(){

        try{
            writer.close();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
