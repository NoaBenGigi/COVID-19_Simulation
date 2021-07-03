//Assignment 1- 
//Noa Ben- Gigi ID:318355633
//Lion Dahan ID:318873338				
package Simulation;

import Country.Map;
import Country.Settlement;
import Population.Healthy;
import IO.SimulationFile;
//import Population.Person;
import java.awt.*;
import java.io.File;
import java.util.Random;
//import java.util.ArrayList;
//import java.util.List;

import Population.Person;
import Population.Sick;
import UI.MainWindow;
//import UI.Slider;
import UI.MutationWindow;
import Virus.BritishVariant;
import Virus.ChineseVariant;
import Virus.IVirus;
import Virus.SouthAfricanVariant;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       MainWindow mainWindow=MainWindow.getInstance();
    }

    public static File loadFileFunc() {
        FileDialog fd = new FileDialog((Frame) null, "Please choose a file:", FileDialog.LOAD);
        fd.setVisible(true);
        if (fd.getFile() == null)
            return null;
        File f = new File(fd.getDirectory(), fd.getFile());
        System.out.println(f.getPath());
        return f;
    }
}