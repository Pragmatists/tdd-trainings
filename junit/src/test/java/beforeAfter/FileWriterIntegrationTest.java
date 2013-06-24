package beforeAfter;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
/**

 TODO: Przekształć testy tak aby były bardziej czytelne. Podpowiedź: użyj @Before i @After.
   
 */
public class FileWriterIntegrationTest {

    @Test
    public void shouldWriteSimpleStringToFile() throws Exception {

        FileWriter writer = null;
        File file = null;

        try{
            // given:
            file = File.createTempFile("FileWriterTest", "");
            writer = new FileWriter(file);
            
            // when:
            writer.writeString("String");
            
            // then:
            Assert.assertEquals("String", readString(file));
            
        } finally{
            closeWriter(writer);
            deleteFile(file);
        }
    }
    
    @Test
    public void shouldWriteStringWithPolishCharactersToFile() throws Exception {
        
        FileWriter writer = null;
        File file = null;
        
        try{
            // given:
            file = File.createTempFile("FileWriterTest", "");
            writer = new FileWriter(file);
            
            // when:
            writer.writeString("Zażółć gęślą jaźń");
            
            // then:
            Assert.assertEquals("Zażółć gęślą jaźń", readString(file));
            
        } finally{
            closeWriter(writer);
            deleteFile(file);
        }
    }
    
    @Test
    public void shouldWriteIntegerToFile() throws Exception {
        
        FileWriter writer = null;
        File file = null;
        
        try{
            // given:
            file = File.createTempFile("FileWriterTest", "");
            writer = new FileWriter(file);
            
            // when:
            writer.writeInteger(42);
            
            // then:
            Assert.assertEquals(42, readInteger(file));
            
        } finally{
            closeWriter(writer);
            deleteFile(file);
        }
    }
    
    @Test
    public void shouldWriteDoubleToFile() throws Exception {
        
        FileWriter writer = null;
        File file = null;
        
        try{
            // given:
            file = File.createTempFile("FileWriterTest", "");
            writer = new FileWriter(file);
            
            // when:
            writer.writeDouble(40.4);
            
            // then:
            Assert.assertEquals(40.4, readDouble(file), 0);
            
        } finally{
            closeWriter(writer);
            deleteFile(file);
        }
    }
    
    @Test
    public void shouldBooleanToFile() throws Exception {
        
        FileWriter writer = null;
        File file = null;
        
        try{
            // given:
            file = File.createTempFile("FileWriterTest", "");
            writer = new FileWriter(file);
            
            // when:
            writer.writeBoolean(true);
            
            // then:
            Assert.assertEquals(true, readBoolean(file));
            
        } finally{
            closeWriter(writer);
            deleteFile(file);
        }
    }
    
    // --
    
    private void deleteFile(File file) {
        if(file != null){
            file.delete();
        }
    }
    
    private void closeWriter(FileWriter writer) throws IOException {
        if(writer != null){
            writer.close();
        }
    }
    
    private String readString(File file) throws IOException, FileNotFoundException {
        return new DataInputStream(new FileInputStream(file)).readUTF();
    }
    
    private int readInteger(File file) throws IOException, FileNotFoundException {
        return new DataInputStream(new FileInputStream(file)).readInt();
    }
    
    private double readDouble(File file) throws IOException, FileNotFoundException {
        return new DataInputStream(new FileInputStream(file)).readDouble();
    }
    
    private boolean readBoolean(File file) throws IOException, FileNotFoundException {
        return new DataInputStream(new FileInputStream(file)).readBoolean();
    }
    
}
