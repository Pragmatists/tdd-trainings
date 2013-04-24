package fileinfo;

import java.awt.GridLayout;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CopyOfFileInfo {

    private String mode;
    
    public CopyOfFileInfo(String mode) {
        this.mode = mode;
    }

    public static void main(String... args) {
        
        String mode = getOption("mode", args);
        
        if(mode == null){
            mode = "console";
        }

        CopyOfFileInfo app = new CopyOfFileInfo(mode);
        app.start(args); 
        
    }

    public void start(String... args) {

        File chosenFile = null;
        if(mode.equals("console")){
            System.out.print("Podaj nazwe pliku: ");
            String filename = new Scanner(System.in).nextLine();
            chosenFile = newFile(filename);
        } else if(mode.equals("gui")){
            JFileChooser fileChooler = newFileChooser();
            int option = fileChooler.showOpenDialog(null);
            if(option != JFileChooser.APPROVE_OPTION)
                throw new IllegalStateException();
            
            chosenFile = fileChooler.getSelectedFile();
        } else if(mode.equals("command")){
            String filename = getOption("file", args);
            chosenFile = newFile(filename);
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

            JFrame frame = newFileInfoFrame();
            frame.setLayout(new GridLayout(-1, 1));
            frame.add(new JLabel(String.format("Nazwa: %s", fileName)));
            frame.add(new JLabel(String.format("Zmodyfikowany: %s", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(lastModified))));
            frame.add(new JLabel(String.format("Rozmiar [B]: %d", size)));
            frame.pack();
            frame.setVisible(true);
        }
    }

    protected JFrame newFileInfoFrame() {
        return new JFrame();
    }

    protected JFileChooser newFileChooser() {
        return new JFileChooser();
    }

    protected File newFile(String filename) {
        return new File(filename);
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
