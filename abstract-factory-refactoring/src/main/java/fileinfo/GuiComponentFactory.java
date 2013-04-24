package fileinfo;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GuiComponentFactory {

    protected JFrame newFileInfoFrame() {
        return new JFrame();
    }

    protected JFileChooser newFileChooser() {
        return new JFileChooser();
    }

    protected JOptionPane newErrorDialog() {
        return new JOptionPane("", JOptionPane.ERROR_MESSAGE);
    }

}
