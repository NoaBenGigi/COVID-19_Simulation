package IO;
import Country.City;
import Country.Kibbutz;
import Country.Map;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.util.Locale;

public class StatisticsFile{

    public StatisticsFile(Map map) throws FileNotFoundException {
        /*String strExcel= null;
        JFileChooser writeFile= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        writeFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int value= writeFile.showSaveDialog(null);
        if(value == JFileChooser.APPROVE_OPTION){
            if(writeFile.getSelectedFile().isDirectory()){
                strExcel= writeFile.getSelectedFile().toString();
                strExcel= strExcel+"statistic.csv";
            }
        }
        File f= new File(strExcel);*/
        FileDialog fd = new FileDialog((Frame) null,"chosse");
        fd.setFile("statistic.csv");
        fd.setVisible(true);
        if(fd.getFile()== null)
            return;
        File f= new File(fd.getDirectory(),fd.getFile());
        System.out.println(f.getPath());
        try (PrintWriter writer = new PrintWriter(f);) {
            StringBuilder sb = new StringBuilder();
            sb.append("Settlement Name");
            sb.append(',');
            sb.append("Settlement type");
            sb.append(',');
            sb.append("Ramzor Color");
            sb.append(',');
            sb.append("Sick People%");
            sb.append(',');
            sb.append("Vaccine Doses");
            sb.append(',');
            sb.append("Died People Number");
            sb.append(',');
            sb.append("People Number");
            sb.append(',');
            sb.append("\n");
            for(int i =0 ; i< map.GetSettlement().size();i++){
                sb.append(map.GetSettlement().get(i).GetName());
                sb.append(",");
                if(map.GetSettlement().get(i) instanceof City){
                    sb.append("City");
                }
                else if(map.GetSettlement().get(i) instanceof Kibbutz){
                    sb.append("Kibbutz");
                }
                else{
                    sb.append("Moshav");
                }
                sb.append(",");
                sb.append(map.GetSettlement().get(i).GetRamzorColor());
                sb.append(",");
                sb.append(map.GetSettlement().get(i).GetSickPeopleList().size()/map.GetSettlement().get(i).GetPeople().size());//number of sick people
                sb.append(",");
                sb.append(map.GetSettlement().get(i).GetNumOfVaccineDoses());
                sb.append(",");
                sb.append(map.GetSettlement().get(i).GetDiedNum());
                sb.append(",");
                sb.append(map.GetSettlement().get(i).GetPeople().size());
                sb.append(",");
                sb.append("\n");
            }
            writer.write(sb.toString());
            writer.close();
        } catch ( FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}
