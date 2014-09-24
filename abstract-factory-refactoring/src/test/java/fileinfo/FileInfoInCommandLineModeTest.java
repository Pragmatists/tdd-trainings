package fileinfo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class FileInfoInCommandLineModeTest {

    private ByteArrayOutputStream output;
    private ByteArrayOutputStream error;
    private ByteArrayInputStream input;

    private File chosenFile;
    private String fileToOpen;

    private FileInfo fileInfo;
    private FileFactory fileFactory;

    @Before
    public void setUp() {
        
        chosenFile = mock(File.class);
        
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        error = new ByteArrayOutputStream();
        System.setErr(new PrintStream(error));
        userEntersFollowingInput("\n");
        
        fileFactory = new FileFactory(){
            
            @Override
            protected File newFile(String filename){
                fileToOpen = filename;
                return  chosenFile;
            }
            
        };
        
        fileInfo = new FileInfo("command", fileFactory);
        
        when(chosenFile.exists()).thenReturn(true);
    }
    
    @Test
    public void shouldNotAskForFile() throws Exception {

        // given:
        // when:
        fileInfo.start();
        
        // then:
        assertThatLineHasNotBeenDisplayed("Podaj nazwe pliku: ");
    }

    @Test
    public void shouldOpenFileRequestedByUser() throws Exception {

        // given:
        fileInfo = new FileInfo("command", fileFactory, "-file", "filename.txt");
        
        // when:
        fileInfo.start();
        
        // then:
        verifyFileHasBeenOpened("filename.txt");
    }
    
    @Test
    public void shouldDisplayFileName() throws Exception {

        // given:
        chosenFileHasName("filename.txt");
        
        // when:
        fileInfo.start();
        
        // then:
        assertThatLineHasBeenDisplayed("Nazwa: filename.txt");
    }

    @Test
    public void shouldDisplayFileSize() throws Exception {

        // given:
        chosenFileHasSize(123);
        
        // when:
        fileInfo.start();
        
        // then:
        assertThatLineHasBeenDisplayed("Rozmiar [B]: 123");
    }
    
    @Test
    public void shouldDisplayFileModificationDate() throws Exception {

        // given:
        chosenFileHasLastModificationDate("2010-10-01 02:03:59");
        
        // when:
        fileInfo.start();
        
        // then:
        assertThatLineHasBeenDisplayed("Zmodyfikowany: 2010-10-01 02:03:59");
    }
    
    @Test
    public void shouldDisplayErrorIfGivenFileDoesNotExist() throws Exception {

        // given:
        chosenFileDoesNotExists();
        
        // when:
        fileInfo.start();
        
        // then:
        assertThatErrorHasBeenDisplayed("Podany plik nie istnieje!");
    }
    
    // --
    

    private void chosenFileDoesNotExists() {
        when(chosenFile.exists()).thenReturn(false);
    }

    private void chosenFileHasLastModificationDate(String lastModified) throws ParseException {
        Long date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(lastModified).getTime();
        when(chosenFile.lastModified()).thenReturn(date);
    }

    private void chosenFileHasSize(long size) {
        when(chosenFile.length()).thenReturn(size);
    }

    private void chosenFileHasName(String filename){
        when(chosenFile.getName()).thenReturn(filename);
    }

    private void assertThatLineHasBeenDisplayed(String expectedText) {
        
        Scanner scanner = new Scanner(output.toString());
        assertLineHasBeenDisplayed(expectedText, scanner);
    }

    private void assertLineHasBeenDisplayed(String expectedText, Scanner scanner) {
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.equals(expectedText))
                return;
        }
    
        Assertions.fail(String.format("Line with text '%s' not found! Actual output:\n%s", expectedText, output.toString()));
    }

    private void assertThatErrorHasBeenDisplayed(String expectedText) {

        Scanner scanner = new Scanner(error.toString());
        assertLineHasBeenDisplayed(expectedText, scanner);
        
    }

    private void assertThatLineHasNotBeenDisplayed(String expectedText) {
        
        Scanner scanner = new Scanner(output.toString());
        
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.equals(expectedText)){
                Assertions.fail(String.format("Line with text '%s' has been found! Actual output:\n%s", expectedText, output.toString()));
            }
        }
    
    }

    private void userEntersFollowingInput(String userInput) {
        input = new ByteArrayInputStream((userInput + "\n").getBytes());
        System.setIn(input);
    }
    
    private void verifyFileHasBeenOpened(String expectedFileToOpen) {
        Assertions.assertThat(fileToOpen).isEqualTo(expectedFileToOpen);
    }
}

