package fileinfo;

import java.io.File;

public class FileFactory {

    protected File newFile(String filename) {
        return new File(filename);
    }

}
