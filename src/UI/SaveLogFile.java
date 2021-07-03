package UI;
import Country.City;
import Country.Kibbutz;
import Country.Map;
import Country.Settlement;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class SaveLogFile {
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    public SaveLogFile(File file) throws FileNotFoundException{
        this.file =file;
        try (PrintWriter writer = new PrintWriter(file);) {
            StringBuilder sb = new StringBuilder();
            sb.append("Settlements Status\n");
            writer.write(sb.toString());
        } catch ( FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    public void writeToFile(Settlement s){
        if (file != null) {
            try (FileWriter sb = new FileWriter(file,true)) {
                synchronized (sb){
                    LocalDateTime now=LocalDateTime.now();
                    String current=now.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
                    sb.write("Settlement: "+ s.GetName());
                    sb.write(" | Real time: " +current);
                    sb.write (" | Number of dead: "+s.GetDiedNum() );
                    sb.write(" | Number of sick: "+ s.GetSicknum() );
                    sb.write("\n");
                }
                sb.flush();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public Memento_LogFile createMemento(){
        return new Memento_LogFile(file);
    }
    public void setMemento(Memento_LogFile memento){
        setFile(memento.getFile());
    }
}

