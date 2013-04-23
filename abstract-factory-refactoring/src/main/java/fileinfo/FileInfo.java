package fileinfo;

import java.awt.GridLayout;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class FileInfo {

    private String mode;
    private FileFactory fileFactory = new FileFactory();
    private GuiComponentFactory guiComponentFactory = new GuiComponentFactory();
    private String[] args;
    
    protected FileInfo(String mode, FileFactory fileFactory, String... args) {
        this.mode = mode;
        this.fileFactory = fileFactory;
        this.args = args;
    }

    protected FileInfo(String mode, GuiComponentFactory guiComponentFactory) {
        this.mode = mode;
        this.guiComponentFactory = guiComponentFactory;
    }

    public FileInfo(String mode, String... args) {
        this.mode = mode;
        this.args = args;
    }

    public static void main(String... args) {
        
        String mode = getOption("mode", args);
        
        if(mode == null){
            mode = "console";
        }

        FileInfo app = new FileInfo(mode, args);
        app.start(); 
        
    }

    public void start() {

        File chosenFile = null;
        if(mode.equals("console")){
            System.out.print("Podaj nazwe pliku: ");
            String filename = new Scanner(System.in).nextLine();
            chosenFile = fileFactory.newFile(filename);
        } else if(mode.equals("gui")){
            JFileChooser fileChooler = guiComponentFactory.newFileChooser();
            fileChooler.showOpenDialog(null);
            chosenFile = fileChooler.getSelectedFile();
        } else if(mode.equals("command")){
            String filename = getOption("file", args);
            chosenFile = fileFactory.newFile(filename);
        }
     
        if(chosenFile == null || !chosenFile.exists()){

            if(mode.equals("console") || mode.equals("command")){
                System.err.println("Podany plik nie istnieje!");
            } else if(mode.equals("gui")){
                JOptionPane error = guiComponentFactory.newErrorDialog();
                error.setMessage("Podany plik nie istnieje!");
                JDialog dialog = error.createDialog("Podany plik nie istnieje!");
                dialog.setVisible(true);
            }
            return;
        }

        // analyze file:
        
        String fileName = chosenFile.getName();
        Date lastModified = new Date(chosenFile.lastModified());
        long size =  chosenFile.length();
        
        if(mode.equals("console") || mode.equals("command")){
            System.out.println("\nFile Info: \n==================================\n");
            System.out.println(String.format("Nazwa: %s", fileName));
            System.out.println(String.format("Zmodyfikowany: %s", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(lastModified)));
            System.out.println(String.format("Rozmiar [B]: %d", size));

        } else if(mode.equals("gui")){

            JFrame frame = guiComponentFactory.newFileInfoFrame();
            frame.setLayout(new GridLayout(-1, 1));
            frame.add(new JLabel(String.format("Nazwa: %s", fileName)));
            frame.add(new JLabel(String.format("Zmodyfikowany: %s", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(lastModified))));
            frame.add(new JLabel(String.format("Rozmiar [B]: %d", size)));
            frame.pack();
            frame.setVisible(true);
        }
    }

    private static String getOption(String option, String[] args) {
    
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            if(arg.equals("-" + option)){
                return args[i+1].toLowerCase();
            }
        }
        return null;
    }
    
}
