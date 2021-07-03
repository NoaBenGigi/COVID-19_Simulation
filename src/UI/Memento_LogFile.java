package UI;

import java.io.File;

public class Memento_LogFile {
    /**
     * This objects' class is going to maintain the state of originator
     * In this program the originator is the SaveLogFile objects.
     */
    private File file;
    public Memento_LogFile(File file){
        this.file = file;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file){
        this.file=file;
    }
}
