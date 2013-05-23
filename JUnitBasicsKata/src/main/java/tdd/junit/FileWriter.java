package tdd.junit;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriter {

    private DataOutputStream outputStream;

    public FileWriter(File file) throws FileNotFoundException {
        outputStream = new DataOutputStream(new FileOutputStream(file));
    }

    public void writeString(String content) throws IOException{
        outputStream.writeUTF(content);
    }
    
    public void writeInteger(Integer content) throws IOException{
        outputStream.writeInt(content);
    }
    
    public void writeDouble(Double content) throws IOException{
        outputStream.writeDouble(content);
    }
    
    public void writeBoolean(Boolean content) throws IOException{
        outputStream.writeBoolean(content);
    }
    
    public void close() throws IOException{
        outputStream.close();
    }
        
}
