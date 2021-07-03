package UI;

import java.io.File;

public class Caretaker_logFile {
    /**
     * This class save the current log file and the previous path of log file
     * this objects' class keeps track of multiple memento.
     */
    private Memento_LogFile curr_file;
    private Memento_LogFile prev_file;
    public Caretaker_logFile(){
        this.curr_file=null;
        this.prev_file=null;
    }
    public void addMemento(Memento_LogFile mementoLogFile){
        if(curr_file != mementoLogFile) {
            prev_file = curr_file;
            curr_file = mementoLogFile;
        }
    }
    public Memento_LogFile getCurr_file(){
        return this.curr_file;
    }
    public Memento_LogFile getPrev_file(){
        return this.prev_file;
    }
}
