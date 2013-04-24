package fileinfo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class FileInfoInGuiModeTest {

    private File chosenFile;

    private JFileChooser fileChooser;
    private JFrame fileInfoFrame;
    private JOptionPane errorDialog;

    private FileInfo fileInfo;

    @Before
    public void setUp() {
        
        fileChooser = mock(JFileChooser.class);
        fileInfoFrame = mock(JFrame.class);
        errorDialog = mock(JOptionPane.class, Mockito.RETURNS_MOCKS);
        
        chosenFile = mock(File.class);
        GuiComponentFactory guiComponentFactory = mock(GuiComponentFactory.class);
        when(guiComponentFactory.newFileChooser()).thenReturn(fileChooser);
        when(guiComponentFactory.newFileInfoFrame()).thenReturn(fileInfoFrame);
        when(guiComponentFactory.newErrorDialog()).thenReturn(errorDialog);
        
        fileInfo = new FileInfo("gui", guiComponentFactory);

        when(fileChooser.getSelectedFile()).thenReturn(chosenFile);
        when(chosenFile.exists()).thenReturn(true);
        
    }
    
    @Test
    public void shouldNotAskForFile() throws Exception {

        // given:
        // when:
        fileInfo.start();
        
        // then:
        verifyFileSelectionPopupHasBeenDisplayed();
    }

    @Test
    public void shouldDisplayFileName() throws Exception {

        // given:
        chosenFileHasName("filename.txt");
        
        // when:
        fileInfo.start();
        
        // then:
        assertLabelHasBeenDisplayed("Nazwa: filename.txt");
    }

    @Test
    public void shouldDisplayFileSize() throws Exception {

        // given:
        chosenFileHasSize(123);
        
        // when:
        fileInfo.start();
        
        // then:
        assertLabelHasBeenDisplayed("Rozmiar [B]: 123");
    }
    
    @Test
    public void shouldDisplayFileModificationDate() throws Exception {

        // given:
        chosenFileHasLastModificationDate("2010-10-01 02:03:59");
        
        // when:
        fileInfo.start();
        
        // then:
        assertLabelHasBeenDisplayed("Zmodyfikowany: 2010-10-01 02:03:59");
    }
    
    @Test
    public void shouldDisplayErrorIfGivenFileDoesNotExist() throws Exception {

        // given:
        chosenFileDoesNotExists();
        
        // when:
        fileInfo.start();
        
        // then:
        assertErrorHasBeenDisplayed("Podany plik nie istnieje!");
    }
    
    @Test
    public void shouldDisplayErrorIfGivenFileHasNotBeenSelected() throws Exception {

        // given:
        fileHasNotBeenSelected();
        
        // when:
        fileInfo.start();
        
        // then:
        assertErrorHasBeenDisplayed("Podany plik nie istnieje!");
    }
    
    // --
    
    private void fileHasNotBeenSelected() {
        when(fileChooser.getSelectedFile()).thenReturn(null);
    }

    private void assertErrorHasBeenDisplayed(String errorMessage) {
        verify(errorDialog).setMessage(errorMessage);
        verify(errorDialog).createDialog(errorMessage);
    }

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

    private void verifyFileSelectionPopupHasBeenDisplayed() {
        verify(fileChooser).showOpenDialog(null);
    }

    private void assertLabelHasBeenDisplayed(String labelContent) {
        verify(fileInfoFrame).setVisible(true);
        
        ArgumentCaptor<JLabel> labels = ArgumentCaptor.forClass(JLabel.class);
        verify(fileInfoFrame, Mockito.atLeastOnce()).add(labels.capture());
        
        for (JLabel label : labels.getAllValues()) {
            if(labelContent.equals(label.getText())){
                return;
            }
        }
        
        Assertions.fail(String.format("Label with text '%s' not found!", labelContent));
    }

}


